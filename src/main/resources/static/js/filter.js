function goToPage(page) {
    document.getElementById('page').value = page;
    document.getElementById('filterForm').submit();
}

document.querySelectorAll('th').forEach(th => {
    if (!th.getAttribute('data')) return;
    th.addEventListener('click', () => {
        updateOrder(th.getAttribute('data'));
    });
})


function updateOrder(field) {
    let form = document.getElementById('filterForm');
    let orderBy = form.elements['orderBy'].value;
    let direction = form.elements['direction'].value;
    if (orderBy === field) {
        form.elements['direction'].value = direction === 'ASC' ? 'DESC' : 'ASC';
    } else {
        form.elements['orderBy'].value = field;
        form.elements['direction'].value = 'ASC';
    }
    updateFilter();
}

document.getElementById('btnSubmit').addEventListener('click', () => {
    updateFilter();
});

function resetField() {
    let form = document.getElementById('filterForm');
    let inputs = document.querySelectorAll('.searchData');
    inputs.forEach(i => {
        i.value = null;
    });
    form.elements['size'].value = 10;
    form.elements['page'].value = 1;
    form.elements['orderBy'].value = 'createdAt';
    form.elements['direction'].value = 'DESC';
    form.submit();
}

function updateFilter() {
    let form = document.getElementById('filterForm');
    const inputs = document.querySelectorAll("input[id$='Field']");
    inputs.forEach(input => {
        let inputId = input.getAttribute('id');
        let inputName = inputId.replace('Field', '');
        if (input.classList.contains('currency-input')) {
            form.elements[inputName].value = input.getAttribute('data-raw');
        } else {
            form.elements[inputName].value = input.value;
        }
    });

    const selects = document.querySelectorAll("select[id$='Field']");
    selects.forEach(select => {
        let selectId = select.getAttribute('id');
        let selectName = selectId.replace('Field', '');
        form.elements[selectName].value = select.value;
    });

    form.submit();
}

document.querySelectorAll('input[id$=Field]').forEach(input => {
    input.addEventListener('keyup', function(event) {
        if (event.key === 'Enter') {
            updateFilter();
        }
    });
});

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.currency-input').forEach(input => {
        input.addEventListener('keydown', function (event) {
            let rawValue = this.getAttribute('data-raw') || '';
            if (event.key === '-' && rawValue.length === 0) {
                this.setAttribute('data-raw', '-');
                this.value = '-';
                event.preventDefault();
                return;
            }

            if (event.key === 'Backspace') {
                event.preventDefault();
                if (rawValue.length > 1) {
                    rawValue = rawValue.slice(0, -1);
                    this.setAttribute('data-raw', rawValue);
                    this.value = formatCurrency(rawValue);
                } else {
                    this.setAttribute('data-raw', '');
                    this.value = '';
                }
            }
        });

        input.addEventListener('input', function () {
            let rawValue = this.value.replace(/[^0-9-]/g, '');

            if (rawValue.includes('-')) {
                rawValue = '-' + rawValue.replace(/-/g, '');
            }

            if (rawValue !== '-' && rawValue !== '-0') {
                rawValue = rawValue.replace(/^(-?)0+/, '$1') || '0';
            }

            this.setAttribute('data-raw', rawValue);
            this.value = formatCurrency(rawValue);
        });

        input.value = formatCurrency(input.getAttribute('data-raw') || '');
    });
});

function formatCurrency(value) {
    if (!value) return '';

    let isNegative = value.startsWith('-');
    let number = value.replace(/[^0-9]/g, '');

    let formatted = Number(number).toLocaleString();
    return isNegative ? '-' + formatted : formatted;
}