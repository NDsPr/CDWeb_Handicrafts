$(document).ready(() => {
    // event delegate (on) nay gọi lại chính nó ($('#search-box'))
     $('#search-box').on('input', () => {
        let searchKey = $('#search-box').val();
        let url = `http://localhost:8080${contextPath}/api/suggest-key`;
        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            data: {key: searchKey},
            success: (response) => {
                displaySuggestions(response);
            },
            error: () => {
                console.log("Lỗi lấy dữ liệu!");
            }
        });
    });
});

// Bắt sự kiện khi người dùng nhấn vào vùng khác
$(document).on('click', (event) => {
    // Kiểm tra xem người dùng có nhấn vào các phần tử tìm kiếm (search box, button, search suggest)
    if (!$(event.target).closest('#search-component').length && !$(event.target).closest('#search-suggest').length) {
        // Người dùng nhấn vào vùng khác, xóa các thẻ suggest
        $('#search-suggest').empty();
        $('#search-suggest').css('display', 'none');
        $('#search-box').val('');
    }
});

function displaySuggestions(suggestKeys) {
    let suggestionsContainer = $('#search-suggest');
    suggestionsContainer.css('display', 'block')
    suggestionsContainer.empty();

    if (suggestKeys.length > 0) {
        for (let i = 0; i < suggestKeys.length; i++) {
            suggestionsContainer.append(`<li class="suggestion result"><a href="#">${suggestKeys[i]}</a></li>`);
        }
    } else {
        suggestionsContainer.append(`<li class="suggestion empty-result">Không có từ khóa gợi ý.</li>`);
    }
}