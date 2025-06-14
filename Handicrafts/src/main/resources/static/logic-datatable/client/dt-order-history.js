$(() => {
    $("#orderHistory").DataTable({
        lengthMenu: [5, 10, 25, 100],
        processing: true,
        serverSide: true,
        ajax: {
            url: "http://localhost:8080/api/client/order-history",
            type: "POST",
            dataType: "json",
            dataSrc: "items",
        },
        columns: [
            {data: "id"},
            {data: "createdDate"},
            {data: "shipToDate"},
            {data: "paymentMethod"},
            {data: "total"},
            {data: "status"},
            // Thiết lập cho cột Thao tác
            {sortable: false, searchable: false,}
        ],
        columnDefs: [
            {
                targets: 1,
                render: (data, type, row, meta) => {
                    if (data != null) {
                        return moment(data).format("DD/MM/YYYY");
                    }
                }
            },
            {
                targets: 2,
                render: (data, type, row, meta) => {
                    if (data != null) {
                        return moment(data).format("DD/MM/YYYY");
                    }
                }
            },
            {
                targets: 4,
                render: (data, type, row, meta) => {
                    return `<span style="color: #d31616; font-weight: 500;">${new Intl.NumberFormat('vi-VN', {
                        style: 'currency',
                        currency: 'VND'
                    }).format(data)}</span>`;
                }
            },
            {
                targets: 5,
                render: (data, type, row, meta) => {
                    switch (data) {
                        case 1:
                            return '<span class="badge bg-primary bg-gradient">Chờ xác nhận</span>';
                        case 2:
                            return '<span class="badge bg-info bg-gradient">Đã xác nhận</span>';
                        case 3:
                            return '<span class="badge bg-warning bg-gradient">Đang vận chuyên</span>';
                        case 4:
                            return '<span class="badge bg-success bg-gradient">Thành công</span>';
                        case 0:
                            return '<span class="badge bg-danger bg-gradient">Đã huỷ</span>';
                        default:
                            return '<span class="badge bg-warning bg-gradient">Lỗi hệ thống</span>';
                    }
                }
            },
            {
                targets: 6,
                render: (data, type, row, meta) => {
                    let allButton = '';
                    const infoButton = `<a href="http://localhost:8080/order-detail-history?orderId=${row.id}" data-bs-toggle="tooltip" title="Chi tiết đơn hàng"
                           class="edit" style="padding: 6px 14px;"><i class="fa-solid fa-info fa-xl" style="color: #e3bd74;"></i></a>`;
                    allButton += infoButton;

                    if (row.status >= 1 && row.status <= 3) {
                        const cancelButton = `<button onclick="cancelOrder(this, ${row.id}, 'order-history')" data-bs-toggle="tooltip" title="Hủy đơn hàng" 
                           class="delete" style="padding: 6px 12px; margin-left: 4px; border: 0;"><i class="fa-solid fa-xmark" style="color: #e3bd74;"></i></button>`;
                        allButton += cancelButton;
                    }

                    if (row.status === 4 ) {
                        const reviewButton = `<button data-bs-toggle="tooltip" title="Đánh giá sản phẩm"
                           class="edit get-form-review" data-send="${row.id}" style="padding: 6px 10px; margin-left: 4px; border: 0;"><i class="fa-solid fa-face-smile" style="color: #e3bd74;"></i></button>`;

                        allButton += reviewButton;
                    }
                    return allButton;
                }
            }
        ]
    });
});