$(() => {
    $('#productData').DataTable({
        scrollX: true,
        lengthMenu: [5, 10, 25, 50],
        processing: true,
        serverSide: true,
        ajax: {
            url: 'http://localhost:8080/api/admin/product',
            type: 'POST',
            dataType: 'json',
            dataSrc: 'items'
        },
        columns: [
            {data: 'id'},
            {data: 'name'},
            {data: 'description'},
            {data: 'categoryTypeId'},
            {data: 'originalPrice'},
            {data: 'discountPrice'},
            {data: 'discountPercent'},
            {data: 'quantity'},
            {data: 'soldQuantity'},
            {data: 'avgRate'},
            {data: 'numReviews'},
            {data: 'images'},
            {data: 'size'},
            {data: 'otherSpec'},
            {data: 'status'},
            {data: 'keyword'},
            {data: 'createdDate'},
            {data: 'createdBy'},
            {data: 'modifiedDate'},
            {data: 'modifiedBy'},
            {sortable: false, searchable: false,}
        ],
        order: [[16, 'desc']],
        columnDefs: [
            {
                targets: 2,
                render: function (data, type, row, meta) {
                    if (data != null) {
                        const escapeHtmlData = escapeHtml(data);
                        return `<button type="button" class="get-des edit" data-bs-toggle="modal" data-send="${escapeHtmlData}" style="border: 0; padding: 10px 14px;">
                                <i class="fa-solid fa-info fa-xl" style="color: #e3bd74;"></i>
                            </button>`;
                    }
                }
            },
            {
                targets: 3,
                render: function (data, type, row, meta) {
                    if (data != null) {
                        return renderCateType(data);
                    }
                }
            },
            {
                targets: 4,
                render: function (data, type, row, meta) {
                    if (data != null) {
                        return `<span>${new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(data)}</span>`;
                    }
                }
            },
            {
                targets: 5,
                render: function (data, type, row, meta) {
                    if (data != null) {
                        return `<span style="color: #d31616; font-weight: 600;">${new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(data)}</span>`;
                    }
                }
            },
            {
                targets: 6,
                render: function (data, type, row, meta) {
                    if (data != null) {
                        return `<span style="color: #d31616; font-weight: 600;">${data}%</span>`;
                    }
                }
            },
            {
                targets: 8,
                render: function (data, type, row, meta) {
                    if (data != null) {
                        return `<span>${data}</span>`;
                    }
                }
            },
            {
                targets: 9,
                render: function (data, type, row, meta) {
                    if (data != null) {
                        return `<span>${data.toFixed(1)}</span>`;
                    }
                }
            },
            {
                targets: 11,
                render: function (data, type, row, meta) {
                    const escapeHtmlData = escapeHtml(JSON.stringify(data));
                    return `<button type="button" class="get-imgs edit" data-bs-toggle="modal" data-send="${escapeHtmlData}" style="border: 0; padding: 10px 14px;">
                                <i class="fa-regular fa-image" style="color: #e3bd74;"></i>
                            </button>`
                }
            },
            {
                targets: 14,
                render: function (data, type, row, meta) {
                    switch (data) {
                        case 0:
                            return '<span class="badge bg-danger bg-gradient">Vô hiệu hóa</span>';
                        case 1:
                            return '<span class="badge bg-success bg-gradient">Còn hàng</span>';
                        case 2:
                            return '<span class="badge bg-warning bg-gradient">Hết hàng</span>';
                        default:
                            return '<span class="badge bg-danger bg-gradient">Lỗi hệ thống</span>';
                    }
                }
            },
            {
                targets: [16, 18],
                render: function (data, type, row, meta) {
                    if (data != null) {
                        return `<span>${moment(data).format("DD/MM/YYYY")}</span>`;
                    } else {
                        return '';
                    }
                }
            },
            {
                targets: 20,
                render: function (data, type, row, meta) {
                    let allButton = '';
                    const updateBtn = `<a href="http://localhost:8080/admin/product-management/editing?id=${row.id}" data-bs-toggle="tooltip" title="Chỉnh sửa sản phẩm"
                       class="edit">
                        <i class="fa-regular fa-pen-to-square" style="color: #e3bd74;"></i>
                    </a>`;

                    const deleteBtn = `<button onclick="deleteRecord(this, ${row.id}, 'blog')" style="padding: 9px; margin-left: 4px;" data-bs-toggle="tooltip" title="Xóa ảnh" class="delete">
                        <i class="fa-solid fa-trash" style="color: #e3bd74;"></i></button>`;

                    allButton += updateBtn;
                    allButton += deleteBtn;
                    return allButton;
                }
            }
        ]
    });
});

function renderCateType(data) {
    switch (data) {
        case 1:
            return 'Lục bình gỗ';
        case 2:
            return 'Đĩa gỗ';
        case 3:
            return 'Tượng gỗ';
        case 4:
            return 'Đồng hồ gỗ';
        case 5:
            return 'Nội thất gỗ';
        case 6:
            return 'Túi xách';
        case 7:
            return 'Ví cầm tay';
        case 8:
            return 'Hộp đựng';
        case 9:
            return 'Nón';
        case 10:
            return 'Nhà thú cưng';
        case 11:
            return 'Thùng rác';
        case 12:
            return 'Khác';
        case 13:
            return 'Bộ ấm trà';
        case 14:
            return 'Lọ';
        case 15:
            return 'Bình phong thủy';
        case 16:
            return 'Tranh';
        case 17:
            return 'Thác nước & tượng';
        case 18:
            return 'Ly, cốc, phin cà phê';
        case 19:
            return 'Khay mứt';
        case 20:
            return 'Bộ bát đĩa';
        default:
            return 'Không xác định';
    }
}