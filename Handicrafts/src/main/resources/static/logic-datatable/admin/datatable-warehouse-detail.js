$(() => {
    $('#warehouseDetailData').DataTable({
        lengthMenu: [5, 10, 25, 100],
        processing: true,
        serverSide: true,
        ajax: {
            url: `http://localhost:8080/api/admin/warehouse-detail?warehouseId=${warehouseId}`,
            type: "POST",
            dataType: "json",
            dataSrc: "items",        // Dữ liệu cần lấy này thuộc tính JSON, ở đây là items (Danh sách)
        },
        columns: [
            {data: "id"},
            {data: "warehouseId"},
            {data: "productId"},
            {data: "quantity"},
        ],
    });
});