const updateCartUrl = `http://localhost:8080${contextPath}/api/cart-updating`;

// Hàm update số lượng và giá tiền khi bấm nút tăng giảm
$(() => {
    // on là hàm event delegate, nghĩa là khi tạo mới thì hàm on sẽ
    // gán sự kiện của nút đó cho elememt mới được render ra
    // Chỗ function không được xài arrow function, sẽ không nhận được giá trị (?????)
    $("#table-content").on("click", ".increase, .decrease", function () {
        // Lấy giá trị số lượng từ ô input tương ứng
        let quantity = $(this).closest('.quantity-container').find('.quantity-amount').val();
        let productId = $(this).closest('.quantity-container').find('.productId').val();

        // Kiểm tra xem nút được nhấn là tăng hay giảm số lượng
        if ($(this).hasClass("increase")) {
            quantity++;
        } else if ($(this).hasClass("decrease")) {
            if (quantity > 0) {
                quantity--;
            }
        }

        // Gửi yêu cầu AJAX đến Servlet
        $.ajax({
            type: "POST",
            url: updateCartUrl,
            dataType: "json",
            data: {
                productId: productId,
                quantity: quantity
            },
            success: (cart) => {
                updateUI(cart);
            },
            error: () => {
                console.log("Lỗi xảy ra khi gửi yêu cầu AJAX!");
            }
        });
    });
});

// Hàm update số lượng và giá tiền khi nhập vào input
$(() => {
    $("#table-content").on("blur", ".quantity-amount", function () {
        let quantity = $(this).closest('.quantity-container').find('.quantity-amount').val();
        let productId = $(this).closest('.quantity-container').find('.productId').val();

        // Gửi yêu cầu AJAX đến Servlet
        $.ajax({
            type: "POST",
            url: updateCartUrl,
            dataType: "json",
            data: {
                productId: productId,
                quantity: quantity
            },
            success: (cart) => {
                updateUI(cart);
            },
            error: () => {
                console.log("Lỗi xảy ra khi gửi yêu cầu AJAX!");
            }
        });
    });
});

// Hàm convert định dạng số
$(() => {
    // Bắt sự kiện khi trang được tải
    $(".main-price, .original-price, .discount-percent, .total").each(() => {
        // Lấy giá trị ban đầu từ text
        let price = parseFloat($(this).text().replace(/\D/g, '')) / 100; // Chuyển đổi từ đơn vị tiền tệ sang số
        // Định dạng lại số tiền và gán vào lại text
        $(this).text(formatCurrency(price));
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
                        <a href="${contextPath}/cart-deleting?productId=${item.product.id}" class="btn btn-black btn-sm">X</a></td>
                    <td class="product-name">
                        <div class="d-flex align-items-center">
                            <img src="${item.product.images[0].link}" alt="Image" class="img-config">
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
                                   name="quantity-<%=i+1%>" value="${item.quantity}"
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

