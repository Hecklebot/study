package bitcamp.chopchop.web;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import bitcamp.chopchop.domain.Comment;
import bitcamp.chopchop.domain.Product;
import bitcamp.chopchop.domain.ProductOption;
import bitcamp.chopchop.service.CommentService;
import bitcamp.chopchop.service.MemberService;
import bitcamp.chopchop.service.ProductOptionService;
import bitcamp.chopchop.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

  @Resource
  private ProductService productService;
  @Resource
  private PhotoFileWriter photoFileWriter;
  @Resource
  private CommentService commentService;
  @Resource
  private MemberService memberService;
  @Resource
  private ProductOptionService productOptionService;


  @GetMapping("form")
  public void form() {}

  @GetMapping("list")
  public void list(Model model) throws Exception {
    model.addAttribute("products", productService.list());
  }

  @PostMapping("add")
  public String add(
      Product product, 
      MultipartFile[] filePath, String[] optionTitle, String[] optionPrice) throws Exception {
    product.setFiles(photoFileWriter.getPhotoFiles(filePath));
    productService.insert(product);
    ProductOption productOption = new ProductOption();
    for (int i=0; i<optionTitle.length; i++) {
      productOption.setProductNo(product.getProductNo());
      productOption.setTitle(optionTitle[i]);
      productOption.setPrice(Integer.parseInt(optionPrice[i]));
      productOptionService.insert(productOption);
    }
    return "redirect:list";
  }

  @GetMapping("delete")
  public String delete(int no) throws Exception {
    productService.delete(no);
    return "redirect:list";
  }

  @GetMapping("detail")
  public void detail(Model model, int no) throws Exception {
    Product product = productService.get(no);
    List<Comment> comments = commentService.findByProductWith(product.getProductNo());
    model.addAttribute("product", productService.get(no));
    model.addAttribute("comments", comments);
  }

  @GetMapping("search")
  public void search(Model model, String keyword) throws Exception {
    List<Product> products = productService.search(keyword);
    model.addAttribute("products", products);
  }

  @GetMapping("category")
  public void categorySearch(String species, Model model) throws Exception {
    List<Product> products = productService.categorySearch(species);
    model.addAttribute("products", products);
  }

  @PostMapping("update")
  public String update(Product product, MultipartFile[] filePath)
      throws Exception {

    product.setFiles(photoFileWriter.getPhotoFiles(filePath));
    productService.update(product);
    return "redirect:list";
  }

  @GetMapping("updateform")
  public void updateform(Model model, int no) throws Exception {
    model.addAttribute("product", productService.get(no));
  }
}


