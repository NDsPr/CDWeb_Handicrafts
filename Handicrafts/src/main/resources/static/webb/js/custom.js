(function () {
    'use strict';

    var tinyslider = function () {
        var el = document.querySelectorAll('.testimonial-slider');

        if (el.length > 0) {
            var slider = tns({
                container: '.testimonial-slider',
                items: 1,
                axis: "horizontal",
                controlsContainer: "#testimonial-nav",
                swipeAngle: false,
                speed: 700,
                nav: true,
                controls: true,
                autoplay: true,
                autoplayHoverPause: true,
                autoplayTimeout: 3500,
                autoplayButtonOutput: false
            });
        }
    };
    tinyslider();
})()

// Dung Pham
// Thay đổi tiêu đề (Footer)
let p_tag = document.getElementById("mxh-changing");
let maxWidth = 768;

function checkScreen() {
    if (window.innerWidth <= maxWidth) {
        p_tag.textContent = "MXH";
    }
    else {
        p_tag.textContent = "Mạng xã hội";
    }
}
checkScreen();
window.addEventListener("resize", checkScreen);

let button = document.getElementById("scroll-to-top");
// Scroll to top (button)
window.onscroll = function scrollToTop() {
    (document.body.scrollTop > 150 || document.documentElement.scrollTop > 150) ? (button.style.display = "block") : (button.style.display = "none");
}
button.addEventListener("click", function () {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
});

// Thay đổi màu khi click button (Trong product-detail.html)
function changeColor(list) {
    for (let i = 0; i < list.length; i++) {
        // Thêm event cho các button
        list[i].addEventListener("click", function () {
            for (let j = 0; j < list.length; j++) {
                // Xóa toàn bộ lớp được chọn (Để reset trạng thái)
                list[j].classList.remove("darkred-active");
            }
            // Sau đó, thêm lớp được chọn vào nút vừa click
            this.classList.add("darkred-active");
        });
    }
}

// Lấy phần tử theo id, từ id đó lấy ra các phần tử có tên class
const myList = document.getElementById("myList")
const listButton = myList.getElementsByClassName("list-group-item-action");
changeColor(listButton);

// Thay đổi cách sắp xếp button khi kích thước màn hình thay đổi
let innerWidth = window.innerWidth;

function checkScreenToArrangeBtn(btnBack) {
    if (innerWidth < lgWidth) {
        btnBack.classList.remove("flex-row", "justify-content-between");
        btnBack.classList.add("flex-column");
        btnBack.firstElementChild.style.marginBottom = "20px"
    }
}

function modifyTagBaseOnWidth(listBtn) {
    for (let i = 0; i < btnBack.length; i++) {
        if (innerWidth < lgWidth) {
            btnBack[i].style.textAlign = "center";
            btnBack[i].style.fontSize = "14";
        }
    }
}

const lgWidth = 992;
const btnBack = document.getElementById("btn-back");
const listBtn = document.getElementsByClassName("back")
checkScreenToArrangeBtn(btnBack)
modifyTagBaseOnWidth(listBtn)

