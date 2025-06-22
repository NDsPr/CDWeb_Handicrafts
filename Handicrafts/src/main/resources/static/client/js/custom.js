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
})();

// Dung Pham
// Thay đổi tiêu đề (Footer)
let p_tag = document.getElementById("mxh-changing");
let maxWidth = 768;

function checkScreen() {
    if (p_tag) {
        if (window.innerWidth <= maxWidth) {
            p_tag.textContent = "MXH";
        } else {
            p_tag.textContent = "Mạng xã hội";
        }
    }
}
checkScreen();
window.addEventListener("resize", checkScreen);

// Scroll to top (button)
let button = document.getElementById("scroll-to-top");
window.onscroll = function scrollToTop() {
    if (button) {
        (document.body.scrollTop > 150 || document.documentElement.scrollTop > 150)
            ? button.style.display = "block"
            : button.style.display = "none";
    }
};
if (button) {
    button.addEventListener("click", function () {
        document.body.scrollTop = 0;
        document.documentElement.scrollTop = 0;
    });
}

// Thay đổi màu khi click button (Trong product-detail.html)
function changeColor(list) {
    for (let i = 0; i < list.length; i++) {
        list[i].addEventListener("click", function () {
            for (let j = 0; j < list.length; j++) {
                list[j].classList.remove("darkred-active");
            }
            this.classList.add("darkred-active");
        });
    }
}

// Lấy phần tử theo id, từ id đó lấy ra các phần tử có tên class
const myList = document.getElementById("myList");
if (myList) {
    const listButton = myList.getElementsByClassName("list-group-item-action");
    changeColor(listButton);
}

// Thay đổi cách sắp xếp button khi kích thước màn hình thay đổi
const lgWidth = 992;
const btnBack = document.getElementById("btn-back");
const listBtn = document.getElementsByClassName("back");

function checkScreenToArrangeBtn(btnBack) {
    if (window.innerWidth < lgWidth) {
        btnBack.classList.remove("flex-row", "justify-content-between");
        btnBack.classList.add("flex-column");
        if (btnBack.firstElementChild) {
            btnBack.firstElementChild.style.marginBottom = "20px";
        }
    }
}

function modifyTagBaseOnWidth(listBtn) {
    for (let i = 0; i < listBtn.length; i++) {
        if (window.innerWidth < lgWidth) {
            listBtn[i].style.textAlign = "center";
            listBtn[i].style.fontSize = "14px";
        }
    }
}

if (btnBack) {
    checkScreenToArrangeBtn(btnBack);
}
if (listBtn.length > 0) {
    modifyTagBaseOnWidth(listBtn);
}
