const suggestUrl = `http://localhost:8080${contextPath}/api/product-suggest`;

$(() => {
    $('.btn-more').on('click', function () {
        let currentPos = $('input[name="currentPos"]').val();
        let categoryTypeId = $('input[name="categoryTypeId"]').val();
        let productId = $('input[name="productId"]').val();

        $.ajax({
            url: suggestUrl,
            type: 'GET',
            dataType: 'json',
            data: {
                currentPos: currentPos,
                categoryTypeId: categoryTypeId,
                productId: productId
            },
            success: (response) => {
                updateUI(response)
            },
            error: (xhr, status, error) => {
                // Xử lý lỗi (nếu có)
                console.error(xhr.responseText);
            }
        });
    });
});

const updateUI = (response) => {
    let suggestContainer = $('.product-suggest');
    let productSuggestWrap = $('.product-suggest-wrap');
    let btnMore = $('.btn-more');
    let currentPos = $('input[name="currentPos"]');
    let productSuggestList = response.productList;

    suggestContainer.remove(btnMore);
    for (const product of productSuggestList) {
        productSuggestWrap.append(`
            <div class="col-2 mb-3">
                <a href="http://localhost:8080${contextPath}/product-detail?id=${product.id}">
                    <div class="card">
                        <span class="discount-percent">${numeral(product.discountPercent).format('0,0')}%</span>
                        <img src="${product.images[0].link}" class="card-img-top suggest-img" alt="...">
                        <div class="card-body d-flex flex-column justify-content-between">
                            <h5 class="card-title">${product.name}</h5>
                            <div class="card-text d-flex flex-column">
                                <p class="discount">${numeral(product.discountPrice).format('0,0')}đ</p>
                                <del class="original">${numeral(product.originalPrice).format('0,0')}đ</del>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
            
        `);
    }

    currentPos.val(response.currentPos);
    suggestContainer.append(btnMore)
};