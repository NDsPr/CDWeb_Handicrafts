const deletingCartUrl = `http://localhost:8080${contextPath}/api/cart-deleting`;
let isPopupVisible = false; // Biến để kiểm tra xem một thông báo có đang hiển thị hay không
let previousPopup = null; // Biến lưu trữ thông báo trước đó (nếu có)

$(() => {
    $(".btn.btn-black.btn-sm").click(function (event) {
        event.preventDefault();
        let itemElement = $(this).closest('tr');
        let productId = itemElement.find('input[name="productId"]').val();

        $.ajax({
            type: "POST",
            url: deletingCartUrl,
            dataType: "json",
            data: {productId: productId},
            success: (response) => {
                // Xử lý phản hồi từ Servlet (nếu cần)
                const totalItems = response.totalItems;
                itemElement.remove();
                notify(response.serverResponse);
                updateTotalItem(totalItems);
            },
            error: (xhr, status, error) => {
                // Xử lý lỗi (nếu có)
                console.error(xhr.responseText);
            }
        });
    });
});

const notify = (serverResponse) => {
    let popup;

    switch (serverResponse) {
        case "success":
            popup = $(`<div id="autoDismissAlert" class="alert alert-success alert-dismissible fade show d-flex align-items-center adding-cart-notify" role="alert">
                        <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Success:"><use xlink:href="#check-circle-fill"/></svg>
                        <div>Xóa sản phẩm khỏi giỏ hàng thành công!</div>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                     </div>`);
            break;

        case "fail":
            popup = $(`<div id="autoDismissAlert" class="alert alert-danger alert-dismissible fade show d-flex align-items-center adding-cart-notify" role="alert">
                        <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:"><use xlink:href="#exclamation-triangle-fill"/></svg>
                        <div>Xóa sản phẩm khỏi giỏ hàng thất bại!</div>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                     </div>`);
            break;

        case "empty":
            popup = $(`<div id="autoDismissAlert" class="alert alert-warning alert-dismissible fade show d-flex align-items-center adding-cart-notify" role="alert">
                        <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:"><use xlink:href="#exclamation-triangle-fill"/></svg>
                        <div>Giỏ hàng không chứa sản phẩm!</div>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                     </div>`);
            break;

        case "invalid":
            popup = $(`<div id="autoDismissAlert" class="alert alert-danger alert-dismissible fade show d-flex align-items-center adding-cart-notify" role="alert">
                        <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:"><use xlink:href="#exclamation-triangle-fill"/></svg>
                        <div>Lỗi từ phía server!</div>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                     </div>`);
            break;
    }

    if (!isPopupVisible) {
        // Nếu không có thông báo nào hiển thị, hiển thị thông báo mới
        $('body').append(popup);
        isPopupVisible = true;
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