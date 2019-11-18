package bitcamp.chopchop.domain;

import java.sql.Date;
import java.util.List;

public class ProductReview {

  private int productReviewNo;
  private int productNo;
  private int memberNo;
  private String filePath;
  private String content;
  private int rating;
  private Date createdDate;

  private List<Product> products;

  public int getProductReviewNo() {
    return productReviewNo;
  }

  public void setProductReviewNo(int productReviewNo) {
    this.productReviewNo = productReviewNo;
  }

  public int getProductNo() {
    return productNo;
  }

  public void setProductNo(int productNo) {
    this.productNo = productNo;
  }

  public int getMemberNo() {
    return memberNo;
  }

  public void setMemberNo(int memberNo) {
    this.memberNo = memberNo;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }
  
    public List<Product> getProducts() {
      return products;
    }
  
    public void setProducts(List<Product> products) {
      this.products = products;
    }

  @Override
  public String toString() {
    return "ProductReview [content=" + content + ", createdDate=" + createdDate + ", filePath=" + filePath
        + ", memberNo=" + memberNo + ", productNo=" + productNo + ", productReviewNo=" + productReviewNo + ", products="
        + products + ", rating=" + rating + "]";
  }
  
}