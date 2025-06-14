$(() => {
    $("#orderDetailHistory").DataTable({
        lengthMenu: [5, 10, 25, 100],
        processing: true,
        serverSide: true,
        ajax: {
            url: `http://localhost:8080/api/client/order-detail-history?orderId=${orderId}`,
            type: "POST",
            dataType: "json",
            dataSrc: "items",
        },
        columns: [
            {data: "productId"},
            {data: "productName"},
            {data: "originalPrice"},
            {data: "discountPrice"},
            {data: "discountPercent"},
            {data: "quantity"},
            {data: "reviewed"}
        ],
        columnDefs: [
            {
                targets: 2,
                render: (data, type, row, meta) => {
                    return `<span>${new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(data)}</span>`;
                }
            },
            {
                targets: 3,
                render: (data, type, row, meta) => {
                    return `<span style="color: #d31616; font-weight: 500;">${new Intl.NumberFormat('vi-VN', {
                        style: 'currency',
                        currency: 'VND'
                    }).format(data)}</span>`
                }
            },
            {
                targets: 4,
                render: (data, type, row, meta) => {
                    return `<span class="badge bg-danger">${data + "%"}</span>`;
                }
            },
            {
                targets: 6,
                render: (data, type, row, meta) => {
                    switch (row.reviewed) {
                        case 1:
                            return `<span class="badge bg-success">Đã đánh giá</span>`;
                        case 0:
                            return `<span class="badge bg-danger">Chưa đánh giá</span>`;
                        default:
                            return `<span class="badge bg-danger">Không hợp lệ</span>`;
                    }
                }
            }
        ]
    });
});