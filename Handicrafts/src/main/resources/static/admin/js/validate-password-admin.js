function validatePasswordAdmin() {
    let oldPassword = document.getElementById("oldPassword").value;
    let newPassword = document.getElementById("newPassword").value;
    let retypePassword = document.getElementById("retypePassword").value;

    // Xóa các thông báo cũ
    document.getElementById("old-error").textContent = "";
    document.getElementById("new-error").textContent = "";
    document.getElementById("retype-error").textContent = "";

    // Kiểm tra xem mật khẩu mới và mật khẩu cũ có trùng nhau không
    if (oldPassword === newPassword && oldPassword !== "" && newPassword !== "") {
        document.getElementById("old-error").textContent = "Mật khẩu mới trùng với mật khẩu cũ.";
        document.getElementById("old-error").style.color = "red";
    }

    // Kiểm tra xem mật khẩu mới và mật khẩu cũ có trùng nhau không
    if (checkPasswordLength(newPassword)) {
        document.getElementById("new-error").textContent = "Mật khẩu phải có ít nhất 6 ký tự.";
        document.getElementById("new-error").style.color = "red";
    }

    if (newPassword !== retypePassword && newPassword !== "" && retypePassword !== "") {
        document.getElementById("retype-error").textContent = "Mật khẩu mới và Nhập lại mật khẩu không trùng nhau.";
        document.getElementById("retype-error").style.color = "red";
    }
}

function checkPasswordLength(password) {
    return password >= 6;
}