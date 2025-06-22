
console.log("cart-update.js loaded!");

// Sử dụng contextPath đã được định nghĩa global
console.log("contextPath:", window.contextPath);
console.log("origin:", window.location.origin);

// cách tạo URL để tránh double slash
const updateCartUrl = window.contextPath === '/'
    ? `${window.location.origin}/api/cart-updating`
    : `${window.location.origin}${window.contextPath}/api/cart-updating`;

console.log("updateCartUrl:", updateCartUrl);

// Hàm update số lượng và giá tiền khi bấm nút tăng giảm
$(document).ready(function() {
    console.log("Document ready!");

    $("#table-content").on("click", ".increase, .decrease", function (e) {
        e.preventDefault();
        console.log("Button clicked!");
        // on là hàm event delegate, nghĩa là khi tạo mới thì hàm on sẽ
        // gán sự kiện của nút đó cho elememt mới được render ra
        // Chỗ function không được xài arrow function, sẽ không nhận được giá trị (?????)
        // Hàm update số lượng và giá tiền khi bấm nút tăng giảm

        // Lấy giá trị số lượng từ ô input tương ứng
        let quantity = parseInt($(this).closest('.quantity-container').find('.quantity-amount').val());
        let productId = parseInt($(this).closest('.quantity-container').find('.productId').val());

        console.log("Before:", {productId, quantity});

        // Kiểm tra xem nút được nhấn là tăng hay giảm số lượng
        if ($(this).hasClass("increase")) {
            quantity++;
        } else if ($(this).hasClass("decrease")) {
            if (quantity > 1) {
                quantity--;
            } else {
                console.log("Cannot decrease below 1");
                return;
            }
        }

        console.log("After:", {productId, quantity});
        console.log("Sending request to URL:", updateCartUrl);

        // Gửi yêu cầu AJAX đến Servlet
        $.ajax({
            type: "POST",
            url: updateCartUrl,
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            data: {
                productId: productId,
                quantity: quantity
            },
            beforeSend: function() {
                console.log("Sending AJAX request...");
            },
            success: function (cart) {
                console.log("Success:", cart);
                updateUI(cart);
            },
            error: function (xhr, status, error) {
                console.log("Error details:", {
                    status: xhr.status,
                    statusText: xhr.statusText,
                    responseText: xhr.responseText,
                    error: error
                });
            }
        });
    });
});
function updateUI(cart) {
    const cartTableContainer = $("#table-content");
    // Làm rỗng tbody
    cartTableContainer.empty();

    if (cart) {
        const items = cart.items;
        for (let item of items) {
            cartTableContainer.append(`
               <tr>
                    <td>
                        <button class="btn btn-black btn-sm delete-item" data-product-id="${item.product.id}">X</button>
                    </td>
                    <td class="product-name">
                        <div class="d-flex align-items-center">
                            <img src="${item.product.images && item.product.images.length > 0 ? item.product.images[0].link : 'https://via.placeholder.com/80x80?text=No+Image'}" alt="Image" class="img-config">
                            <h2 class="h5 m-0 ps-4 text-black">${item.product.name}</h2>
                        </div>
                    </td>
                    <td>
                        <div class="d-flex flex-column mb-2">
                            <div class="main-price d-flex justify-content-start">${formatCurrency(item.product.discountPrice)}đ</div>
                            <div class="d-flex flex-row">
                                <div class="original-price">
                                    <del>${formatCurrency(item.product.originalPrice)}đ</del>
                                </div>
                                <div class="discount-percent">${formatCurrency(item.product.discountPercent)}%</div>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="input-group d-flex align-items-center quantity-container"
                             style="max-width: 120px;">
                            <div class="input-group-prepend">
                                <button class="btn btn-outline-black decrease" type="button">&minus;
                                </button>
                            </div>
                            <input type="hidden" class="productId" name="productId"
                                   value="${item.product.id}">
                            <input type="text" class="form-control text-center quantity-amount"
                                   name="quantity" value="${item.quantity}"
                                   placeholder="" aria-label="Example text with button addon"
                                   aria-describedby="button-addon1">
                            <div class="input-group-append">
                                <button class="btn btn-outline-black increase" type="button">&plus;
                                </button>
                            </div>
                        </div>
                    </td>
                    <td class="total">${formatCurrency(item.totalWithDiscount)}đ</td>
               </tr>
            `)
        }
    }

    const totalContainer = $("#total-container");
    totalContainer.empty();
    totalContainer.append(`
        <div class="row mb-3">
            <div class="col-md-6">
                <span class="text-black">Tổng giá gốc</span>
            </div>
            <div class="col-md-6 text-right">
                <del class="text-black original-total">${formatCurrency(cart.originalPriceTotal)}đ</del>
            </div>
        </div>
        <div class="row mb-5">
            <div class="col-md-6">
                <span class="text-black">Tổng giá sau giảm</span>
            </div>
            <div class="col-md-6 text-right">
                <strong class="text-danger discount-total">${formatCurrency(cart.discountPriceTotal)}đ</strong>
            </div>
        </div>
    `)
}

// Hàm định dạng số tiền
function formatCurrency(number) {
    // Chuyển đổi số về chuỗi
    let convertNumber = parseInt(number);
    return convertNumber.toLocaleString('en-US');
}
