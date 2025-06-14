$(document).ready(function () {
    $('#logData').DataTable({
        scrollX: true,
        lengthMenu: [5, 10, 25, 100],
        processing: true,
        serverSide: true,
        ajax: {
            url: "http://localhost:8080/api/admin/log",
            type: "POST",
            dataType: "json",
            dataSrc: "items",
        },
        columns: [
            {data: "id"},
            {data: "ip"},
            {data: "national"},
            {data: "level"},
            {data: "address", sortable: false, searchable: false},
            {data: "previousValue", sortable: false, searchable: false},
            {data: "currentValue", sortable: false, searchable: false},
            {data: "createdDate"},
            {data: "createdBy"},
            // Thiết lập cho cột Thao tác
            {sortable: false, searchable: false,}
        ],
        order: [[7, "desc"]],
        columnDefs: [
            {
                target: 3,
                render: function (data, type, row, meta) {
                    switch (data) {
                        case 1:
                            return '<span class="badge bg-secondary bg-gradient px-3 py-2">Info</span>';
                        case 2:
                            return '<span class="badge bg-info bg-gradient px-3 py-2">Alert</span>';
                        case 3:
                            return '<span class="badge bg-warning bg-gradient px-3 py-2">Warning</span>';
                        case 4:
                            return '<span class="badge bg-danger bg-gradient px-3 py-2">Danger</span>';
                        default:
                            return '<span class="badge bg-warning bg-gradient px-3 py-2">Lỗi hệ thống</span>';
                    }
                }
            },
            {
                targets: [4, 5, 6],
                render: function (data, type, row, meta) {
                    // Dữ liệu được gửi qua show-modal.js
                    if (data === null) {
                        return `<pre style="white-space: pre-wrap; margin: 0;"><code class="json">null</code></pre>`;
                    }
                    const escapeHtmlData = escapeHtml(data);
                    return `<button type="button" class="open-modal-button edit" data-bs-toggle="modal" data-send="${escapeHtmlData}" style="border: 0; padding: 10px 14px;">
                                <i class="fa-solid fa-info fa-xl" style="color: #e3bd74;">
                            </button>`
                }
            },
            {
                target: 9,
                render: function (data, type, row, meta) {
                    let allButton = '';
                    const deleteBtn = `<button type="button" onclick="deleteRecord(this, ${row.id}, 'log')" style="padding: 9px; margin-left: 4px;" data-bs-toggle="tooltip" title="Xóa ảnh" class="delete">
                        <i class="fa-solid fa-trash" style="color: #e3bd74;"></i></button>`;
                    allButton += deleteBtn;
                    return allButton;
                }
            }
        ]
    });
});