// Thêm biến toàn cục để theo dõi tính hợp lệ của form
let isValid = true;

function checking() {
    // Đặt lại giá trị isValid khi bắt đầu xác thực
    isValid = true;

    // Lấy giá trị của email và password
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
    let retypePassword = document.getElementById("retypePassword").value;

    // Xóa bất kỳ thông báo lỗi cũ nào
    document.getElementById("emailError").textContent = "";
    document.getElementById("passwordError").textContent = "";
    document.getElementById("retypePasswordError").textContent = "";

    // Kiểm tra tính hợp lệ của email và password
    if (!isValidEmail(email) && email !== "") {
        document.getElementById("emailError").textContent = "Email không hợp lệ.";
        document.getElementById("emailError").style.display = 'block';
        isValid = false;
    }

    if (!isValidPassword(password) && password !== "") {
        document.getElementById("passwordError").textContent = "Mật khẩu có ít nhất 6 ký tự.";
        document.getElementById("passwordError").style.display = 'block';
        isValid = false;
    }

    if (!isValidPassword(retypePassword) && retypePassword !== "") {
        document.getElementById("retypePasswordError").textContent = "Mật khẩu có ít nhất 6 ký tự.";
        document.getElementById("retypePasswordError").style.display = 'block';
        isValid = false;
    }

    if (password !== retypePassword && password !== "" && retypePassword !== "") {
        document.getElementById("passwordError").textContent = "Mật khẩu và Nhập lại mật khẩu không trùng khớp."
        document.getElementById("retypePasswordError").textContent = "Mật khẩu và Nhập lại mật khẩu không trùng khớp."
        document.getElementById("passwordError").style.display = 'block';
        document.getElementById("retypePasswordError").style.display = 'block';
        isValid = false;
    }

    return isValid;
}

// Hàm kiểm tra tính hợp lệ của email
function isValidEmail(email) {
    // Sử dụng biểu thức chính quy để kiểm tra tính hợp lệ
    let emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]+$/;
    return emailPattern.test(email);
}

// Hàm kiểm tra tính hợp lệ của password
function isValidPassword(password) {
    return password.length >= 6;
}

function togglePassword(inputId) {
    const passwordInput = document.getElementById(inputId);
    const toggleButton = document.getElementById(`toggle_${inputId}`);

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleButton.classList.remove('fa-eye');
        toggleButton.classList.add('fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        toggleButton.classList.remove('fa-eye-slash');
        toggleButton.classList.add('fa-eye');
    }
}

// Thêm đoạn mã này vào cuối file
document.addEventListener('DOMContentLoaded', function() {
    // Lấy phần tử form
    const form = document.getElementById('registerForm');

    // Thêm sự kiện lắng nghe khi gửi form
    if (form) {
        form.addEventListener('submit', function(event) {
            // Xác thực tất cả các trường trước khi gửi
            if (!validateForm()) {
                event.preventDefault(); // Ngăn gửi form nếu không hợp lệ
            }
        });
    }

    // Khởi tạo hiển thị thông báo lỗi
    const errorElements = document.querySelectorAll('.error-message');
    errorElements.forEach(element => {
        if (!element.textContent.trim()) {
            element.style.display = 'none';
        } else {
            element.style.display = 'block';
        }
    });
});

// Hàm xác thực đầy đủ form
function validateForm() {
    isValid = true;

    // Lấy tất cả giá trị đầu vào
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
    let retypePassword = document.getElementById("retypePassword").value;

    // Xóa các lỗi trước đó
    document.getElementById("emailError").textContent = "";
    document.getElementById("passwordError").textContent = "";
    document.getElementById("retypePasswordError").textContent = "";

    // Xác thực email
    if (email.trim() === "") {
        document.getElementById("emailError").textContent = "Email không được để trống!";
        document.getElementById("emailError").style.display = 'block';
        isValid = false;
    } else if (!isValidEmail(email)) {
        document.getElementById("emailError").textContent = "Email không hợp lệ.";
        document.getElementById("emailError").style.display = 'block';
        isValid = false;
    }

    // Xác thực mật khẩu
    if (password.trim() === "") {
        document.getElementById("passwordError").textContent = "Mật khẩu không được để trống!";
        document.getElementById("passwordError").style.display = 'block';
        isValid = false;
    } else if (!isValidPassword(password)) {
        document.getElementById("passwordError").textContent = "Mật khẩu có ít nhất 6 ký tự.";
        document.getElementById("passwordError").style.display = 'block';
        isValid = false;
    }

    // Xác thực nhập lại mật khẩu
    if (retypePassword.trim() === "") {
        document.getElementById("retypePasswordError").textContent = "Nhập lại mật khẩu không được để trống!";
        document.getElementById("retypePasswordError").style.display = 'block';
        isValid = false;
    } else if (password !== retypePassword) {
        document.getElementById("retypePasswordError").textContent = "Mật khẩu và Nhập lại mật khẩu không trùng khớp.";
        document.getElementById("retypePasswordError").style.display = 'block';
        isValid = false;
    }

    return isValid;
}
