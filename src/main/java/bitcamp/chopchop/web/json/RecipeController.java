package bitcamp.chopchop.web.json;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import bitcamp.chopchop.domain.Ingredient;
import bitcamp.chopchop.domain.Member;
import bitcamp.chopchop.domain.Recipe;
import bitcamp.chopchop.domain.RecipeLike;
import bitcamp.chopchop.service.MemberService;
import bitcamp.chopchop.service.RecipeCommentService;
import bitcamp.chopchop.service.RecipeService;
import bitcamp.chopchop.web.CookingFileWriter;

@RestController("json.RecipeController")
@RequestMapping("/json/recipe")
public class RecipeController {
  @Resource private RecipeService recipeService;
  @Resource private CookingFileWriter cookingFileWriter;
  @Resource private MemberService memberService;
  @Resource private RecipeCommentService recipeCommentService;

  String uploadDir;

  public RecipeController(ServletContext sc) {
    uploadDir = sc.getRealPath("/upload/recipe");
  }

  @PostMapping("add")
  public JsonResult add(Recipe recipe, int memberNo, MultipartFile filePath, MultipartFile[] filePath2, String[] ingredientNames, String[] quantity, String[] cookingContent, 
      int[] processNo) throws Exception {

    try {
      String filename = UUID.randomUUID().toString();
      recipe.setThumbnail(filename);
      filePath.transferTo(new File(uploadDir + "/" + filename));

      List<Ingredient> ingredients = new ArrayList<>();
      for (int i = 0; i < ingredientNames.length; i++) {
        Ingredient temp = new Ingredient();
        temp.setName(ingredientNames[i]);
        temp.setQuantity(quantity[i]);
        ingredients.add(temp);
      }

      recipe.setCookings(cookingFileWriter.getCookings(filePath2, processNo, cookingContent));
      recipe.setIngredients(ingredients);
      recipeService.insert(recipe);
      return new JsonResult().setState(JsonResult.SUCCESS);
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }

  @GetMapping("delete")
  public JsonResult delete(int no) throws Exception {
    try {
      recipeService.delete(no);
      return new JsonResult().setState(JsonResult.SUCCESS);
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }

  @GetMapping("detail")
  public JsonResult detail(int no, HttpSession session) throws Exception {
    try {
      Recipe recipe = recipeService.get(no);
      Member member = memberService.get(recipe.getMemberNo());
      Member viewer = (Member) session.getAttribute("loginUser");
      
      RecipeLike recipeLike = new RecipeLike();
      recipeLike.setMemberNo(viewer.getMemberNo());
      recipeLike.setRecipeNo(recipe.getRecipeNo());
      int check = recipeService.findLike(recipeLike);
      boolean likeCheck = false;
      if (check == 1) { // 좋아요햇음
        likeCheck = true;
      } else if (check == 0){ // 좋아요 안했음
        likeCheck = false;
      }

      JsonResult jsonResult = new JsonResult();
      HashMap<String,Object> hashMap = new HashMap<>();
      hashMap.put("member", member);
      hashMap.put("recipe", recipe);
      hashMap.put("isCheck", likeCheck);
      hashMap.put("viewer", viewer);
      jsonResult.setState(JsonResult.SUCCESS).setResult(hashMap);
      return jsonResult;
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }

  @GetMapping("updateform")
  public JsonResult updateform(int no) throws Exception {
    try {
      Recipe recipe = recipeService.get(no);
      return new JsonResult().setState(JsonResult.SUCCESS).setResult(recipe);
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }

  @PostMapping("update")
  public JsonResult update(Recipe recipe, int memberNo, MultipartFile filePath, MultipartFile[] filePath2, String[] ingredientNames, String[] quantity, String[] cookingContent, 
      int[] processNo) throws Exception {
    try {
      String filename = UUID.randomUUID().toString();
      recipe.setThumbnail(filename);

      filePath.transferTo(new File(uploadDir + "/" + filename));

      List<Ingredient> ingredients = new ArrayList<>();
      for (int i = 0; i < ingredientNames.length; i++) {
        Ingredient temp = new Ingredient();
        temp.setName(ingredientNames[i]);
        temp.setQuantity(quantity[i]);
        ingredients.add(temp);
      }

      recipe.setCookings(cookingFileWriter.getCookings(filePath2, processNo, cookingContent));
      recipe.setIngredients(ingredients);
      recipeService.update(recipe);
      return new JsonResult().setState(JsonResult.SUCCESS);
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }

  @GetMapping("list")
  public JsonResult list(@RequestParam(defaultValue = "1") int pageNo,
                         @RequestParam(defaultValue = "4") int pageSize) throws Exception {
    // 총 페이지 개수 알아내기
    if (pageSize < 4 || pageSize > 20) {
      pageSize = 4;
    }
    int size = recipeService.size();
    int totalPage = size / pageSize;
    if (size % pageSize > 0) {
      totalPage++;
    }
    
    // 요청하는 페이지 번호가 유효하지 않을 때는 기본 값으로 1페이지를 지정한다.
    if (pageNo < 1 || pageNo > totalPage) {
      pageNo = 1;
    }
    
    try {
      List<Recipe> recipes = recipeService.list(pageNo, pageSize);
      
      HashMap<String,Object> result = new HashMap<>();
      result.put("recipes", recipes);
      result.put("pageNo", pageNo);
      result.put("pageSize", pageSize);
      result.put("totalPage", totalPage);
      result.put("size", size);
      
      return new JsonResult().setState(JsonResult.SUCCESS).setResult(result);
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }
  
  @GetMapping("listSort")
  public JsonResult listSort(@RequestParam(defaultValue = "recipe_id") String column) throws Exception {
    
    try {
      List<Recipe> recipes = recipeService.listSort(column);
      return new JsonResult().setState(JsonResult.SUCCESS).setResult(recipes);
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }
  
  @GetMapping("listCategory")
  public JsonResult listCategory(String category) throws Exception {
    try {
      List<Recipe> originrecipes = recipeService.listSort("recipe_id");
      List<Recipe> recipes = new ArrayList<>();
      
      for (Recipe recipe : originrecipes) {
        if (category.equals("0")) {
          recipes.add(recipe);
        } else if (recipe.getCategory().equals(category)) {
          recipes.add(recipe);
        }
      }
      return new JsonResult().setState(JsonResult.SUCCESS).setResult(recipes);
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }

  @GetMapping("search")
  public JsonResult search(String keyword) throws Exception {
    try {
      List<Recipe> recipes = recipeService.search(keyword);
      return new JsonResult().setState(JsonResult.SUCCESS).setResult(recipes);
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }

  @GetMapping("like")
  public JsonResult like(int no, HttpSession session) throws Exception {
    try {
      Member member = (Member)session.getAttribute("loginUser");
      Recipe recipe = recipeService.get(no);

      RecipeLike recipeLike = new RecipeLike();
      recipeLike.setMemberNo(member.getMemberNo());
      recipeLike.setRecipeNo(recipe.getRecipeNo());

      JsonResult jsonResult = new JsonResult();
      HashMap<String,Object> hashMap = new HashMap<>();
      
      recipe = recipeService.get(no);
      
      if (recipeService.findLike(recipeLike) == 1) { // 좋아요 취소해야함
        recipeService.deleteLike(recipeLike);
        hashMap.put("recipeNo", recipe.getRecipeNo());
        hashMap.put("member", member);
        hashMap.put("isLike", false);
        hashMap.put("scrap", recipeService.get(no).getScrap());
        jsonResult.setState(JsonResult.SUCCESS).setResult(hashMap);
        return jsonResult;

      } else { // 좋아요 를 눌렀음
        recipeService.insertLike(recipeLike);
        
        hashMap.put("recipeNo", recipe.getRecipeNo());
        hashMap.put("member", member);
        hashMap.put("isLike", true);
        hashMap.put("scrap", recipeService.get(no).getScrap());
        jsonResult.setState(JsonResult.SUCCESS).setResult(hashMap);
        return jsonResult;
      }
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }
  
  //=========================================================================================
  
  @GetMapping("myrecipe")
  public JsonResult myList(Model model, HttpSession session) throws Exception {
    try {
      Member member = (Member) session.getAttribute("loginUser");
      List<Recipe> recipes = recipeService.listSort("recipe_id");
      List<Recipe> myrecipes = new ArrayList<>(); // 내 레시피 목록을 담을 리스트 준비
      for (Recipe recipe : recipes) {
        if (recipe.getMemberNo() != member.getMemberNo()) 
          continue;
        System.out.println("스크랩수우우우" + recipe.getScrap());
        myrecipes.add(recipe);
      }
      return new JsonResult().setState(JsonResult.SUCCESS).setResult(myrecipes);
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }
  
  @GetMapping("myscrap")
  public JsonResult scrapList(Model model, HttpSession session) throws Exception {
    try {
      Member member = (Member) session.getAttribute("loginUser");
      List<RecipeLike> recipeLikes = recipeService.listLike(); 
      List<Recipe> scrapRecipes = new ArrayList<>(); 
      for (RecipeLike recipeLike : recipeLikes) {
        if (recipeLike.getMemberNo() == member.getMemberNo()) {
          scrapRecipes.add(recipeService.get(recipeLike.getRecipeNo()));
        }
      }
      return new JsonResult().setState(JsonResult.SUCCESS).setResult(scrapRecipes);
    } catch (Exception e) {
      return new JsonResult().setState(JsonResult.FAILURE).setMessage(e.getMessage());
    }
  }
}
