function inputingCheck() {
    // Lấy giá trị của mật khẩu cũ, mật khẩu mới và nhập lại mật khẩu mới
    let newPassword = document.getElementById("newPassword").value;
    let retypePassword = document.getElementById("retypePassword").value;

    // Xóa bất kỳ thông báo lỗi cũ nào
    document.getElementById("newPasswordInputErr").textContent = "";
    document.getElementById("newPasswordSpaceErr").textContent = "";
    document.getElementById("retypePasswordInputErr").textContent = "";
    document.getElementById("retypePasswordSpaceErr").textContent = "";

    // Kiểm tra xem trong mật khẩu có chứa khoảng trắng không
    if (newPassword.includes(" ")) {
        document.getElementById("newPasswordSpaceErr").textContent = "Mật khẩu không được chứa khoảng trắng"
    }
    if (retypePassword.includes(" ")) {
        document.getElementById("retypePasswordSpaceErr").textContent = "Mật khẩu không được chứa khoảng trắng"
    }

    // Kiểm tra tính hợp lệ của mật khẩu mới
    if (!isValidPassword(newPassword) && newPassword !== "") {
        document.getElementById("newPasswordInputErr").textContent = "Mật khẩu mới phải có ít nhất 6 kí tự.";
    }

    // Kiểm tra xem mật khẩu mới có trùng với nhập lại mật khẩu mới hay không
    if (newPassword !== retypePassword && newPassword !== "" && retypePassword !== "") {
        document.getElementById("retypePasswordInputErr").textContent = "Mật khẩu mới và nhập lại mật khẩu không trùng khớp.";
    }
}

// Hàm kiểm tra tính hợp lệ của password
function isValidPassword(password) {
    return password.length >= 6;
}