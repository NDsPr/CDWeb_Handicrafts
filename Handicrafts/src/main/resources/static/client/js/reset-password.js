function renewPassword() {
        // Lấy giá trị của mật khẩu cũ, mật khẩu mới và nhập lại mật khẩu mới
        var password1 = document.getElementById("oldPassword").value;
        var password2 = document.getElementById("newPassword").value;
        var password3 = document.getElementById("retypePassword").value;

        // Xóa bất kỳ thông báo lỗi cũ nào
        document.getElementById("oldpw-error").textContent = "";
        document.getElementById("newpw-error").textContent = "";
        document.getElementById("retype-error").textContent = "";

        // Kiểm tra tính hợp lệ của mật khẩu cũ
        if (password1 === password2 && password1 !== "" && password2 !== "") {
            document.getElementById("oldpw-error").textContent = "Mật khẩu mới trùng với mật khẩu cũ.";
            document.getElementById("oldpw-error").style.color = "red";
        }

        // Kiểm tra tính hợp lệ của mật khẩu mới
        if (!isValidPassword(password2) && password2 !== "") {
            document.getElementById("newpw-error").textContent = "Mật khẩu mới phải có ít nhất 6 kí tự.";
            document.getElementById("newpw-error").style.color = "red";
        }

        // Kiểm tra xem mật khẩu mới có trùng với nhập lại mật khẩu mới hay không
        if (password2 !== password3 && password2 !== "" && password3 !== "") {
            document.getElementById("retype-error").textContent = "Mật khẩu mới và nhập lại mật khẩu không trùng khớp.";
            document.getElementById("retype-error").style.color = "red";
        }
}


// Hàm kiểm tra tính hợp lệ của password
function isValidPassword(password) {
    return password.length >= 6;
}

