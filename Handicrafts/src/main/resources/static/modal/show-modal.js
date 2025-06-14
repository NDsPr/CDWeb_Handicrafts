$(document).ready(function () {
    // Khi mở nút thông tin log (log-management)
    $("body").on("click", ".open-modal-button", function () {
        console.log("clicked");

        // Lấy dữ liệu từ data-send
        const data = $(this).attr("data-send");
        const jsonData = JSON.parse(data);
        $('.json-content').html(`<pre style="white-space: pre-wrap; margin: 0;"><code class="json">${JSON.stringify(jsonData, null, 2)}</code></pre>`)

        $("#showJsonData").modal('show');
    });

    // Khi ấn vào nút đánh giá (order-history)
    $("body").on("click", ".get-form-review", function () {
        console.log("clicked");
        const buttonClicked = $(this);
        const orderId = parseInt(buttonClicked.attr("data-send"));
        console.log(orderId);

        $.ajax({
            type: "GET",
            url: "http://localhost:8080/api/client/review-form",
            dataType: "json",
            data: {
                orderId: orderId
            },
            success: function (response) {
                renderListFormReview(response);
            },
            error: function (xhr, status, error) {
                console.log(error);
            }
        });

        $("#reviewProduct").modal('show');
    });

    // Khi ấn vào nút hiển thị des trong product-management
    $("body").on("click", ".get-des", function () {
        console.log("clicked");
        const buttonClicked = $(this);
        const data = buttonClicked.attr("data-send");

        $('.description-content').html(data);
        $("#showDescription").modal('show');
    });

    // Khi ấn vào nút hiển thị ảnh
    $("body").on("click", ".get-imgs", function () {
        console.log("clicked");
        const buttonClicked = $(this);
        const jsonData = JSON.parse(buttonClicked.attr("data-send"));

        renderImgs(jsonData);

        $("#showImgs").modal('show');
    });
});

const bodyReview = $('#reviewProduct .modal-body');

function renderListFormReview(response) {
    console.log(response);
    // Làm rỗng modal trước
    bodyReview.empty();

    if (response.length > 0) {
        // Lấy danh sách imgUrl

        for (const reviewForm of response) {
            const form = `
            <div class="form-container mb-4">
                <form id="reviewForm-product${reviewForm.productId}">
                    <div class="form-group">
                        <h3>Đánh giá sản phẩm ${reviewForm.productName}</h3>
                    </div>
                    <div class="form-group">
                        <!--Ảnh sẽ xuất hiện ở đây-->
                        <div class="img-block my-2 d-flex">
                            
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Đánh giá:</label><br>
                        <span class="star-rating">
                            <label for="rate-1" style="--i:1"><i class="fa-solid fa-star"></i></label>
                            <input type="radio" name="rating" id="rate-1" value="1">
                            <label for="rate-2" style="--i:2"><i class="fa-solid fa-star"></i></label>
                            <input type="radio" name="rating" id="rate-2" value="2">
                            <label for="rate-3" style="--i:3"><i class="fa-solid fa-star"></i></label>
                            <input type="radio" name="rating" id="rate-3" value="3">
                            <label for="rate-4" style="--i:4"><i class="fa-solid fa-star"></i></label>
                            <input type="radio" name="rating" id="rate-4" value="4">
                            <label for="rate-5" style="--i:5"><i class="fa-solid fa-star"></i></label>
                            <input type="radio" name="rating" id="rate-5" value="5" checked>
                        </span>
                    </div>
                    <div class="form-group">
                        <label for="comment">Bình luận:</label>
                        <textarea class="form-control" id="content" name="content" rows="3" required></textarea>
                    </div>
                    <input type="hidden" name="userId" value="${userIdToForm}" required>
                    <input type="hidden" name="username" value="${userNameToForm}" required>
                    <input type="hidden" name="productId" value="${reviewForm.productId}" required>
                    <input type="hidden" name="productName" value="${reviewForm.productName}" required">
                    <input type="hidden" name="orderId" value="${reviewForm.orderId}"">
                    <button type="button" onclick="sendData(this)" data-form-id="reviewForm-product${reviewForm.productId}" class="btn btn-primary mt-3">Gửi đánh giá</button>
                </form>
            </div>
            <hr>`;
            bodyReview.append(form);

            // Sau khi đã có phần body của review, thêm ảnh vào img-block
            for (const imgUrl of reviewForm.productImgs) {
                const imgBlock = $(`#reviewForm-product${reviewForm.productId} .img-block`);
                imgBlock.empty();
                imgBlock.append(`<div><img class="img-fluid" style="height: 70px" src="${imgUrl}" alt="Product image"></div>`);
            }
        }
    }
}

function sendData(buttonClicked) {
    // Bắt sự kiện click button
    console.log("btn clicked");
    const formId = $(buttonClicked).attr("data-form-id");
    const formSent = $(`#${formId}`);

    // Serialize data vè URL-encoded
    const formData = formSent.serialize();
    const formContainer = formSent.parent();

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/client/review-form",
        data: formData,
        dataType: "json",
        success: function (response) {
            console.log(response)
            notify(response, formContainer, formSent);
        },
        error: function (xhr, status, error) {
            console.log(error);
            formContainer.html(
                `<div class="alert alert-danger">
                                Đã xảy ra lỗi khi gửi đánh giá. Vui lòng thử lại sau.
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>`
            );
        }
    });
}

function notify(message, formContainer) {
    const status = message["status"];
    const notify = message["notify"];

    if (status === "success") {
        formContainer.html(
            `<div class="alert alert-success">
                    ${notify}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>`
        );
    } else {
        formContainer.before(
            `<div class="alert alert-danger">
                  ${notify}
                  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
             </div>`
        );
    }
}

function renderImgs(data) {
    const indicators = $('.carousel-indicators');
    const inner = $('.carousel-inner');

    indicators.empty();
    inner.empty();

    for (const [index, image] of data.entries()) {
        const indicator = `
            <button type="button" data-bs-target="#imgsCarousel" data-bs-slide-to="${index}" ${index === 0 ? 'class="active" aria-current="true"' : ''} aria-label="Slide ${index + 1}"></button>
        `;

        const carouselItem = `
            <div class="carousel-item ${index === 0 ? 'active' : ''}">
                <img src="${image.link}" class="d-block w-100" style="height: 350px;" alt="Image">
            </div>
        `;

        indicators.append(indicator);
        inner.append(carouselItem);
    }
}