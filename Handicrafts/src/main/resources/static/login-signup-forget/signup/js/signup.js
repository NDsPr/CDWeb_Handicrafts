function checking() {
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
        isValid = false;
    }

    if (!isValidPassword(password) && password !== "") {
        document.getElementById("passwordError").textContent = "Mật khẩu có ít nhất 6 ký tự.";
        isValid = false;
    }

    if (!isValidPassword(retypePassword) && retypePassword !== "") {
        document.getElementById("retypePasswordError").textContent = "Mật khẩu có ít nhất 6 ký tự.";
        isValid = false;
    }

    if (password !== retypePassword && password !== "" && retypePassword !== "") {
        document.getElementById("passwordError").textContent = "Mật khẩu và Nhập lại mật khẩu không trùng khớp."
        document.getElementById("retypePasswordError").textContent = "Mật khẩu và Nhập lại mật khẩu không trùng khớp."
        isValid = false;
    }
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



