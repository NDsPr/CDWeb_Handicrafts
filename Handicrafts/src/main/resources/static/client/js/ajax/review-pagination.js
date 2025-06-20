const reviewUrl = `http://localhost:8080${contextPath}/api/client/pagination-review`;
let current = 1;
let totalReviewPages = Math.ceil(totalReview / 2);
let rateFilter = 0;

$(() => {
    window.pagObj = $('#pagination').twbsPagination({
        // Kiểm tra các thuộc tính trong file .js (trong default)
        totalPages: totalReviewPages,
        // Đây là SỐ PAGE HIỂN THỊ TRONG THANH CHỌN PAGE
        visiblePages: 5,
        startPage: current,
        onPageClick: (event, page) => {
            // Nếu page hiện tại khác page đang chọn thì không cần fetch data
            if (current !== page) {
                current = page;
                fetchReviewData(page);
            }
        }
    });

    $('#filter-rating').on('change', function () {
        handleRatingFilter(this);
    });
});

function fetchReviewData(page) {
    $.ajax({
        url: reviewUrl,
        type: "GET",
        dataType: "json",
        data: {
            currentPage: (page - 1),
            rating: rateFilter,
            productId: productId
        },
        success: (response) => {
            updateReviewUI(response);
        },
        error: (xhr, status, error) => {
            console.log(error);
            console.log(xhr);
        }
    });
}

function handleRatingFilter(buttonClicked) {
    rateFilter = parseInt($(buttonClicked).val());
    current = 1;
    fetchReviewData(current);
}

function updateReviewUI(response) {
    const reviewContainer = $('.rate-list');
    reviewContainer.empty();

    if (!response || response.length <= 0) {
        reviewContainer.append("<p class='text-center'>Không có bình luận nào</p>");
    } else {
        for (let review of response) {
            const reviewItem =
                `<div class="rate-item">
                 <div class="name-date d-flex">
                     <div class="name me-2" style="font-weight: bold">${review.username}</div>
                     <div class="date">${moment(review.createdDate, "MMM D, YYYY, h:mm:ss A").format("DD/MM/YYYY")}</div>
                 </div>
                 <div class="rating">
                     ${renderStar(review.rating)}
                 </div>
                 <div class="comment">${review.content}</div>
            </div>
            <hr>`;

            reviewContainer.append(reviewItem);
        }
    }
}

function renderStar(avgRating) {
    let stars = '';
    for (let i = 1; i <= 5; i++) {
        if (i <= avgRating) {
            stars += '<i class="fa-star fa-solid" style="color: #FFD43B;"></i>';
        } else {
            stars += '<i class="fa-star fa-regular" style="color: #FFD43B;"></i>';
        }
    }
    return stars;
}