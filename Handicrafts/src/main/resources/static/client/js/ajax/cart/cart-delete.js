console.log("cart-delete.js loaded!");

// Sử dụng contextPath từ window object
const contextPath = window.contextPath || '';
const deletingCartUrl = contextPath === '/'
    ? `${window.location.origin}/api/cart-deleting`
    : `${window.location.origin}${contextPath}/api/cart-deleting`;

console.log("Delete URL:", deletingCartUrl);

let isPopupVisible = false;
let previousPopup = null;

$(document).ready(function() {
    console.log("Delete script ready!");
    console.log("Found delete buttons:", $(".btn.btn-black.btn-sm").length);

    // Sử dụng event delegation
    $(document).on("click", ".btn.btn-black.btn-sm", function (event) {
        event.preventDefault();
        console.log("Delete button clicked!");

        let itemElement = $(this).closest('tr');
        let productId = $(this).data('product-id') || itemElement.find('input[name="productId"]').val();

        console.log("Deleting product ID:", productId);
        console.log("Delete URL:", deletingCartUrl);

        $.ajax({
            type: "POST",
            url: deletingCartUrl,
            dataType: "json",
            data: {productId: productId},
            beforeSend: function() {
                console.log("Sending delete request...");
            },
            success: (response) => {
                console.log("Delete success:", response);
                const totalItems = response.totalItems;
                itemElement.remove();
                notify(response.serverResponse);
                updateTotalItem(totalItems);
            },
            error: (xhr, status, error) => {
                console.error("Delete error:", {
                    status: xhr.status,
                    statusText: xhr.statusText,
                    responseText: xhr.responseText,
                    error: error
                });
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
        $('body').append(popup);
        isPopupVisible = true;
        previousPopup = popup;
    } else {
        if (previousPopup) {
            previousPopup.remove();
        }
        $('body').append(popup);
        previousPopup = popup;
    }

    setTimeout(() => {
        $("#autoDismissAlert").alert('close');
        isPopupVisible = false;
    }, 3000);
}

const updateTotalItem = (totalItems) => {
    const totalItemElement = $(".number-item");
    totalItemElement.text(totalItems);
}
