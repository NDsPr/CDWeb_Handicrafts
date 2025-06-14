function checkEmail() {
    // Lấy giá trị của email
    let email = document.getElementById("email").value;

    // Xóa bất kỳ thông báo lỗi cũ nào
    document.getElementById("emailError").textContent = "";

    // Kiểm tra tính hợp lệ của email và password
    if (!isValidEmail(email) && email !== "") {
        document.getElementById("emailError").textContent = "Email không đúng.";
        document.getElementById("emailError").style.borderColor = "red";
    }
}

// Hàm kiểm tra tính hợp lệ của email
function isValidEmail(email) {
    // Sử dụng biểu thức chính quy để kiểm tra tính hợp lệ
    let emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+(\.[a-zA-Z]+)+$/;
    return emailPattern.test(email);
}
