const addingCartUrl = `http://localhost:8080${contextPath}/api/cart-adding`;
let isPopupVisible = false; // Biến để kiểm tra xem một thông báo có đang hiển thị hay không
let previousPopup = null; // Biến lưu trữ thông báo trước đó (nếu có)


// Function gửi yêu cầu AJAX khi click vào nút "Thêm vào giỏ hàng"
// Đối với "/shop", "/shop-detail-by-category", "/shop-detail-by-type" là .product-item, còn với "/product-detail" là .cart-div
$(() => {
    // Lấy ra button giỏ hàng (.left)
    $(".btn-pop-mini.left").click(function (event) {
        event.preventDefault(); // Ngăn chặn hành động mặc định của nút

        const productId = $(this).closest('.product-item, .cart-div').find('input[name="productId"]').val(); // Lấy productId của sản phẩm

        // Gửi yêu cầu AJAX đến Servlet để thêm sản phẩm vào giỏ hàng
        $.ajax({
            type: "POST",
            url: addingCartUrl,
            dataType: "json",
            data: {productId: productId},
            success: (response) => {
                // Xử lý phản hồi từ Servlet (nếu cần)
                if (response.success === "true") {
                    const totalItems = response.totalItems;
                    notify();
                    updateTotalItem(totalItems);
                    // Xử lý response.sendRedirect được Servlet gửi trên client-side
                } else if (response.redirectLink) {
                    // Thực hiện chuyển hướng bằng cách cập nhật URL của trang
                    window.location.href = response.redirectLink;
                }
            },
            error: (xhr, status, error) => {
                // Xử lý lỗi (nếu có)
                console.error(xhr.responseText);
            }
        });
    });
});

const notify = () => {
    const popup = $(`<div id="autoDismissAlert" class="alert alert-success alert-dismissible fade show d-flex align-items-center adding-cart-notify" role="alert">
                        <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Success:"><use xlink:href="#check-circle-fill"/></svg>
                        <div>Thêm vào giỏ hàng thành công!</div>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                     </div>`);

    if (!isPopupVisible) {
        // Nếu không có thông báo nào hiển thị, hiển thị thông báo mới
        $('body').append(popup);
        isPopupVisible = true;
        // Cập nhật thông báo hiện tại vào previous
        previousPopup = popup
    } else {
        // Nếu có thông báo đang hiển thị, xóa thông báo cũ khỏi DOM
        previousPopup.remove();
        // Hiển thị thông báo mới
        $('body').append(popup);
        // Cập nhật thông báo trước đó với thông báo mới
        previousPopup = popup;
    }

    // Đóng popup sau 3s
    setTimeout(() => {
        $("#autoDismissAlert").alert('close');
        isPopupVisible = false; // Cập nhật trạng thái hiển thị thông báo
    }, 3000);
}


const updateTotalItem = (totalItems) => {
    const totalItemElement = $(".number-item");
    totalItemElement.text(totalItems);
}