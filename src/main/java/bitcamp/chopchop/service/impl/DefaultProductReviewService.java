package bitcamp.chopchop.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import bitcamp.chopchop.dao.ProductReviewDao;
import bitcamp.chopchop.domain.ProductReview;
import bitcamp.chopchop.service.ProductReviewService;

@Service
public class DefaultProductReviewService implements ProductReviewService {

  @Resource
  private ProductReviewDao productReviewDao;

  @Override
  public void insert(ProductReview productReview) throws Exception {
    productReviewDao.insert(productReview);
  }
  
  @Override
  public void delete(int no) throws Exception {
    if (productReviewDao.delete(no) == 0) {
      throw new Exception("해당 데이터가 없습니다.");
    }
  }

  @Override
  public ProductReview get(int no) throws Exception {
    ProductReview productReview = productReviewDao.findBy(no);
    if (productReview == null) {
      throw new Exception("해당 번호의 데이터가 없습니다!");
    } 
    return productReview;
  }

  @Override
  public List<ProductReview> list(int no) throws Exception {
    return productReviewDao.findAll();
  }
  
  @Override
  public void update(ProductReview productReview) throws Exception {
    productReviewDao.update(productReview);
  }
}

















