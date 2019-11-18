<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<html>
  
<style>
  .txt_post {
    font-size: 16px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 4;
    -webkit-box-orient: vertical;
    word-wrap: break-word;
    line-height: 1.5em;
    height: 6.0em;
  }

  #myModal {
    top: 50%;
    margin-top: -325px;
  }

  #myAddModal {
    top: 50%;
    margin-top: -240px;
  }

  #myUpdateModal {
    top: 50%;
    margin-top: -240px;
  }
</style>

<body onload="formLoad()">

  <div>
    <h4 class="mb-50">상품 후기</h4>
    
    <div class="row">
      <div class="col-md-5">
        <c:set var="total" value="0" />
        <c:forEach items="${productReviews}" var="productReview" varStatus="status">
          <input type="hidden" value="${productReview.rating}" />
          <input type="hidden" value="${status.count}" />
          <c:set var="count" value="${status.count}" />
          <c:set var="total" value="${total + productReview.rating}" />
          <c:set var="avg" value="${total / count}" />
        </c:forEach>
        <p
          style="color:red; margin-bottom: 25; font-size: 25px; font-weight: 5000px; vertical-align: middle; text-align: right;">
          <c:if test="${avg >= 0 && avg < 1}">☆☆☆☆☆</c:if>
          <c:if test="${avg >= 1 && avg < 2}">★☆☆☆☆</c:if>
          <c:if test="${avg >= 2 && avg < 3}">★★☆☆☆</c:if>
          <c:if test="${avg >= 3 && avg < 4}">★★★☆☆</c:if>
          <c:if test="${avg >= 4 && avg < 5}">★★★★☆</c:if>
          <c:if test="${avg == 5}">★★★★★</c:if>
        </p>
      </div>
      <div class="col-md-1">
        <p style="color:red; margin-bottom: 25; font-size: 25px; font-weight: 5000px; vertical-align: middle;">
          ${Math.round(avg*10)/10}
        </p>
      </div>
      <div class="col-md-1">
        <p style="color:gray; margin-bottom: 25; font-size: 25px; font-weight: 5000px; vertical-align: middle;">│</p>
      </div>
      <div class="col-md-5">
        <p style="color:black; margin-bottom: 25; font-size: 25px; font-weight: 5000px; vertical-align: middle;">
          ${count} REVIEWS
        </p>
      </div>
    </div>
    <table class='table table-hover'>
      <tr style="text-align: center;">
        <th style="width: 130px">사진</th>
        <th>내용</th>
        <th style="width: 150px">작성자</th>
        <th style="width: 150px">평점</th>
        <th style="width: 150px">등록일</th>
      </tr>
      <c:forEach items="${productReviews}" var="productReview">
        <tr class="tempTr" style="text-align: center;" data-toggle="modal" data-target="#myModal">
          <input type="hidden" class="tempProductReviewNo" value="${productReview.productReviewNo}">
          <td style="width: 100px; height: 100px; object-fit: cover"><img src='/upload/productreview/${productReview.filePath}'></td>
          <td style="text-align: left"><span class="txt_post">${productReview.content}</span></td>
          <td>${productReview.memberNo}</td>
          <td style='color: red;' value='${productReview.rating}'>
            <c:if test="${productReview.rating == 1}">★☆☆☆☆</c:if>
            <c:if test="${productReview.rating == 2}">★★☆☆☆</c:if>
            <c:if test="${productReview.rating == 3}">★★★☆☆</c:if>
            <c:if test="${productReview.rating == 4}">★★★★☆</c:if>
            <c:if test="${productReview.rating == 5}">★★★★★</c:if>
          </td>
          <td>${productReview.createdDate}
          </td>
        </tr>
      </c:forEach>
    </table>

    <button type="button" class="btn btn-primary" data-toggle="modal" data-target=".bd-example-modal-lg">후기등록</button>


  </div>

  <!-- detail Modal -->

  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLongTitle">상품 후기</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="row">
              <div class="col-md-8">
                <span id="tempImg" style="width: 500px; height: 500px; object-fit: cover">이미지</span>
              </div>
              <div class="col-md-4" style="padding-left: 0px;">
                <div style="height: 50px; text-align: center;">
                  <input type="hidden" id="tempModalProductReviewNo" value="0">
                  <div style='color: red; font-size: 25px;'>
                  <span id="tempStar">별점</span>
                  </div>
                  <span id="tempId">아이디</span>
                  <span> | </span>
                  <span id="tempDate">날짜</span>
                </div>
                <hr>
                <div style="overflow:auto; height: 350px;">
                  <span id="tempCont">내용</span>
                </div>
                <hr>
                <div style="height: 38; text-align: right;">
                    <button type="button" class="btn btn-danger" onclick="reviewDel()" style="width: 60px;">삭제</button>
                    <button type="button" class="btn btn-primary" style="width: 60px;" data-dismiss="modal" data-toggle="modal" data-target=".bd-example-modal-lg-update">수정</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" style="width: 120px;">닫기</button>
                  </div>
              </div>
          </div>
        </div>
      </div>
    </div>
  </div>

<!-- add modal -->

<div class="modal fade bd-example-modal-lg" id="myAddModal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel"
  aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle">상품후기 등록</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">

        <form action='../productreview/add' enctype='multipart/form-data' method='post'>
          <input type='hidden' name='productNo' id='productNo' value='${product.productNo}'><br>
          
          <div class="row">
                <div class="col-md-4">
                  <div>
            <input type="hidden" id="photo" value="${productReview.filePath}" />
            <img id="imgThumb" class="imgThumb" style="width: 250px; height: 250px; object-fit: cover">
          </div>
            <input type='file' id='fileupload' name='file' /><br>
          </div>
          <div class="col-md-8">
          <select class="custom-select" name='rating'>
              <option selected value="5">★★★★★ [아주좋아요]</option>
              <option value="4">★★★★☆ [맘에들어요]</option>
              <option value="3">★★★☆☆ [보통이에요]</option>
              <option value="2">★★☆☆☆ [그냥그래요]</option>
              <option value="1">★☆☆☆☆ [별로에요]</option>
            </select>
            <hr>
            <!-- <input type="text" class="form-control" name="content" style="overflow:auto; height: 200px;"> -->
            <textarea class="form-control" id="message-text" name="content" style="overflow:auto; height: 200px; resize: none;"></textarea>
        </div>
      </div>
        <hr>
        <div style="text-align: right;">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
            <button type="submit" class="btn btn-primary">등록하기</button>
            <!-- <button type="submit" class="btn btn-primary">등록하기</button> -->
          </div>
        </form>

      </div>
    </div>
  </div>
</div>

<!-- update modal -->

<div class="modal fade bd-example-modal-lg-update" id="myUpdateModal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel"
  aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle">상품후기 수정</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">

        <form action='../productreview/update' enctype='multipart/form-data' method='post'>
          <input type='hidden' name='productNo' id='productNo' value='${product.productNo}'><br>
          <input type='hidden' name='productReviewNo' id='productReviewNo' value='${product.productNo}'><br>
          
          <div class="row">
                <div class="col-md-4">
                  <div>
            <input type="hidden" id="photo" value="${productReview.filePath}" />
            <img id="imgThumb" class="imgThumb" style="width: 250px; height: 250px; object-fit: cover">
          </div>
            <input type='file' id='fileupload' name='file' /><br>
          </div>
          <div class="col-md-8">
          <select class="custom-select" name='rating'>
              <option selected value="5">★★★★★ [아주좋아요]</option>
              <option value="4">★★★★☆ [맘에들어요]</option>
              <option value="3">★★★☆☆ [보통이에요]</option>
              <option value="2">★★☆☆☆ [그냥그래요]</option>
              <option value="1">★☆☆☆☆ [별로에요]</option>
            </select>
            <hr>
            <!-- <input type="text" class="form-control" name="content" style="overflow:auto; height: 200px;"> -->
            <textarea class="form-control" id="tempContUpdate" name="content" style="overflow:auto; height: 200px; resize: none;"></textarea>
        </div>
      </div>
        <hr>
        <div style="text-align: right;">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
            <button type="submit" class="btn btn-primary">수정하기</button>
            <!-- <button type="submit" class="btn btn-primary">등록하기</button> -->
          </div>
        </form>

      </div>
    </div>
  </div>
</div>



  <script>

// 리스트

 $(document).on("click", ".tempTr", function () {
   tempImg.innerHTML = $(this)[0].cells[0].innerHTML;
   tempCont.innerText = $(this)[0].cells[1].innerText;
   tempId.innerText = $(this)[0].cells[2].innerText;
   tempStar.innerText = $(this)[0].cells[3].innerText;
   tempDate.innerText = $(this)[0].cells[4].innerText;

   tempContUpdate.innerText = $(this)[0].cells[1].innerText;

   tempModalProductReviewNo.value = $(this).context.childNodes[1].defaultValue;
   productReviewNo.value =  $(this).context.childNodes[1].defaultValue;
});

// 등록

function formLoad() {
      // hidden값을 이용해서 자바스크립트를 이용한 경우
      if (document.getElementById("photo").value == null
        || document.getElementById("photo").value == "") {
        document.getElementById("imgThumb").src = "/upload/productreview/info_photo.jpg";
      } else {
        document.getElementById("imgThumb").src = "/upload/productreview/"
          + document.getElementById("photo").value;
      }
    }

    document.getElementById("fileupload").onchange = function () {
      var reader = new FileReader();
      reader.onload = function (e) {
        document.getElementById("imgThumb").src = e.target.result;
      };
      reader.readAsDataURL(this.files[0]);
    };

    // 삭제

      function reviewDel() {
        var selectedReviewNo = document.getElementById("tempModalProductReviewNo").value;
        if (confirm("진짜 삭제할거가?")) {
          location.href = "../productreview/delete?no=" + selectedReviewNo + "&productNo=${product.productNo}"; 
          alert("삭제했다!")
        }
    }

  </script>

</body>

</html>