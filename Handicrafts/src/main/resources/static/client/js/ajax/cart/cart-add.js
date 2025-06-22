$(() => {
    console.log("contextPath:", contextPath);

    // Fix contextPath nếu undefined
    if (!contextPath || contextPath === '') {
        contextPath = '';
    }

    const addingCartUrl = `${contextPath}/api/cart-adding`;
    console.log("URL:", addingCartUrl);

    $(".btn-pop-mini.left").click(function (event) {
        event.preventDefault();

        let productId = $(this).data('product-id');

        if (!productId) {
            productId = $(this).closest('.product-item').find('input[name="productId"]').val();
        }

        if (!productId) {
            alert("Không thể xác định sản phẩm!");
            return;
        }

        console.log("ProductId:", productId);

        $.ajax({
            type: "POST",
            url: addingCartUrl,
            dataType: "json",
            data: {
                productId: productId,
                quantity: 1
            },
            success: function(response) {
                console.log("Response:", response);
                if (response.success === "true") {
                    notify();
                    updateTotalItem(response.totalItems);
                }
            },
            error: function(xhr, status, error) {
                console.error("URL:", addingCartUrl);
                console.error("Status:", status);
                console.error("Error:", error);
                alert("Lỗi kết nối!");
            }
        });
    });
});

const notify = () => {
    const popup = $(`<div id="autoDismissAlert" class="alert alert-success alert-dismissible fade show d-flex align-items-center adding-cart-notify" role="alert">
                        <div>Thêm vào giỏ hàng thành công!</div>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                     </div>`);

    $('body').append(popup);

    setTimeout(() => {
        $("#autoDismissAlert").alert('close');
    }, 3000);
}

const updateTotalItem = (totalItems) => {
    $(".number-item").text(totalItems);
}
