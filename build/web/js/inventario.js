let galletas = [];
let paquetes = [];

initialize();

function initialize() {
    refreshTableInventario();
    refreshTablePaquete();
    refreshTableGalletas();
}

async function saveGalleta() {

    let url = "http://localhost:8080/DonGalleto/api/inv/save";
    let params = null;
    let resp = null;
    let datos = null;
    let gall = null;

    gall = new Object();
    gall.idGalleta = 0;

    if (document.getElementById("txtIdGalleta").value.trim().length > 0) {
        gall.idGalleta = parseInt(document.getElementById("txtIdGalleta").value.trim());
    }

    gall.tipo = document.getElementById("txtTipo").value;
    gall.sabor = document.getElementById("txtSabor").value;
    gall.tipo = document.getElementById("txtCantidad").value;
    gall.tipo = document.getElementById("txtFecha").value;
    gall.tipo = document.getElementById("txtEstatus").value;


    params = {
        datosGalleta: JSON.stringify(gall)
    };

    let ctype = 'application/x-www-form-urlencoded;charset=UTF-8';
    resp = await fetch(url,
            {
                method: "POST",
                headers: {'Content-Type': ctype},
                body: new URLSearchParams(params)
            });
    datos = await resp.json();
    if (datos.error != null) {
        Swal.fire('Error al guardar el lote.', datos.error, 'warning');
        return;
    }
    if (datos.exception != null) {
        Swal.fire('', datos.exception, 'warning');
        return;
    }

    // Refrescamos el catalogo de libros:
    fillTableInventario();
    refreshTableInventario();
    Swal.fire('Movimiento Realizado',
            'Lote Guardado correctamente.',
            'success');
}

function clearForm() {
    document.getElementById("txtIdGalleta").value = '';
    document.getElementById("txtEstatus").value = '';
    document.getElementById("txtTipo").value = '';
    document.getElementById("txtSabor").value = '';
    document.getElementById("txtCantidad").value = '';
    document.getElementById("txtFecha").value = '';
}

async function refreshTableInventario() {

    let url = "http://localhost:8080/DonGalleto/api/inv/getAll";
    let resp = await fetch(url);
    let datos = await resp.json();

    if (datos.error != null) {
        alert("Error");
        Swal.fire('', datos.error, 'warning');
        return;
    }
    if (datos.exception != null) {
        Swal.fire('', datos.error, 'warning');
        return;
    }
    galletas = datos;
    fillTableInventario();
}

function fillTableInventario() {
    let contenido = '';

    for (let i = 0; i < galletas.length; i++) {
        contenido += `<tr data-cantidad="${galletas[i].cantidad}">
            <td>${galletas[i].idGalleta}</td>
            <td>${galletas[i].sabor}</td>
            <td>${galletas[i].tipo}</td>
            <td>${galletas[i].fechaCad}</td>
            <td>${galletas[i].cantidad}</td>
            <td>
                <a href="#" class="text-warning" onclick="cargarDetalleGalleta(${i});">
                    <i class="fa fa-info-circle"></i>
                </a>
            </td>
        </tr>`;
    }
    document.getElementById("tBodyInventario").innerHTML = contenido;
    resaltarInventario();
}

function resaltarInventario() {
    const filas = document.querySelectorAll("#tBodyInventario tr");

    filas.forEach((fila) => {
        const cantidad = parseInt(fila.dataset.cantidad, 10);

        // Verifica si la cantidad es menor a 30 y aplica estilo
        if (cantidad <= 30) {
            fila.style.backgroundColor = "rgba(255, 0, 0, 0.1)";
        }
    });
}

function cargarDetalleGalleta(index) {
    const galleta = galletas[index];

    document.getElementById("txtIdGalleta").value = galleta.idGalleta || '';
    document.getElementById("txtEstatus").value = galleta.estatus || '';
    document.getElementById("txtSabor").value = galleta.sabor || '';
    document.getElementById("txtTipo").value = galleta.tipo || '';
    document.getElementById("txtCantidad").value = galleta.cantidad || '';
    document.getElementById("txtFecha").value = galleta.fechaCad || '';
}

async function mermar() {
    let url = "http://localhost:8080/DonGalleto/api/inv/del";
    let params = null;
    let resp = null;
    let datos = null;
    let gall = null;

    gall = new Object();

    gall.idGalleta = 0;
    if (document.getElementById("txtIdGalleta").value.trim() > 0) {
        gall.idGalleta = parseInt(document.getElementById("txtIdGalleta").value.trim());
    }

    gall.estatus = document.getElementById("txtEstatus").value;

    const cantidadInput = document.getElementById("mdlCantidad").value.trim();
    const mermarTodo = document.getElementById("flexCheckIndeterminate").checked;

    // Si "Mermar Todo" está marcado, cantidad = 0; de lo contrario, usa la cantidad ingresada o 0 si está vacía
    gall.cantidad = mermarTodo ? 0 : (parseInt(cantidadInput, 10) || 0);

    params = {eliminacion: JSON.stringify(gall)};

    let ctype = 'application/x-www-form-urlencoded;charset=UTF-8';
    try {
        resp = await fetch(url, {
            method: "POST",
            headers: {'Content-Type': ctype},
            body: new URLSearchParams(params) // Convertir el objeto params a JSON
        });

        if (!resp.ok) {
            throw new Error(`¡Error HTTP! Estado: ${resp.status}`);
        }

        datos = await resp.json();
        console.log("Datos recibidos: ", datos);

        Swal.fire('Movimiento Realizado', 'Lote Mermado correctamente.', 'success');
        console.log(resp);
    } catch (error) {
        Swal.fire('Error al Mermar el Lote', error.message, 'warning');
        console.log(error);
    }
    clearForm();
    fillTableInventario();
    refreshTableInventario();
}

async function refreshTableGalletas() {

    let url = "http://localhost:8080/DonGalleto/api/inv/getAll";
    let resp = await fetch(url);
    let datos = await resp.json();

    if (datos.error != null) {
        alert("Error");
        Swal.fire('', datos.error, 'warning');
        return;
    }
    if (datos.exception != null) {
        Swal.fire('', datos.error, 'warning');
        return;
    }
    galletas = datos;
    fillTableGalletas();

}

function fillTableGalletas() {
    let contenido = '';

    for (let i = 0; i < galletas.length; i++) {
        contenido += `<tr data-id="${galletas[i].id}" data-cantidad="${galletas[i].cantidad}" onclick="rellenarIdGalleta(${i});">
            <td>${galletas[i].sabor}</td>
            <td>${galletas[i].tipo}</td>
            <td>${galletas[i].cantidad}</td>
        </tr>`;
    }
    document.getElementById("tBodyGalletas").innerHTML = contenido;
}

function rellenarIdGalleta(index) {
    // Obtener el ID de la galleta seleccionada
    let idGalleta = galletas[index].idGalleta;

    // Asignar el valor al input del modal
    document.getElementById("mdlIdGalletas").value = idGalleta;

    // Abrir el modal si es necesario (si no se está abriendo automáticamente)
    $('#modalNuevoPaquete').modal('show');
}

async function refreshTablePaquete() {
    let url = "http://localhost:8080/DonGalleto/api/inv/gaPkg";
    let resp = await fetch(url);
    let datos = await resp.json();

    if (datos.error != null) {
        alert("Error");
        Swal.fire('', datos.error, 'warning');
        return;
    }
    if (datos.exception != null) {
        Swal.fire('', datos.error, 'warning');
        return;
    }
    paquetes = datos;
    fillTablePaquete();
}

function fillTablePaquete() {
    let contenido = '';

    for (let i = 0; i < paquetes.length; i++) {
        contenido += `<tr data-cantidad="${paquetes[i].cantidad}">
            <td>${paquetes[i].idPaquete}</td>  
            <td>${paquetes[i].galleta.sabor}</td>  
            <td>${paquetes[i].tipo}</td>
            <td>${paquetes[i].galleta.fechaCad}</td> 
            <td>${paquetes[i].cantidad}</td> 
            <td>
                <a href="#" class="text-warning" onclick="(${i});">
                    <i class="fa fa-info-circle"></i>
                </a>
            </td>
        </tr>`;
    }
    document.getElementById("tBodyPaquetes").innerHTML = contenido;
    resaltarPaquetes(); // Llama a la función para resaltar paquetes con pocas existencias
}

function resaltarPaquetes() {
    const filas = document.querySelectorAll("#tBodyPaquetes tr");

    filas.forEach((fila) => {
        const cantidad = parseInt(fila.dataset.cantidad, 10);

        if (cantidad <= 2) {
            fila.style.backgroundColor = "rgba(255, 0, 0, 0.1)";
        }
    });
}

async function savePaquete() {

    let url = "http://localhost:8080/DonGalleto/api/inv/savePkg";
    let params = null;
    let resp = null;
    let datos = null;

    let pkg = null;
    let idGalleta = 0;
    let idPaquete = 0;

    pkg = new Object();
    pkg.idPaquete = 0;

    pkg.galleta = new Object();
    pkg.galleta.idGalleta = 0;

    if (document.getElementById("mdlIdGalletas").value.trim().length > 0) {
        pkg.galleta.idGalleta = parseInt(document.getElementById("mdlIdGalletas").value.trim());
    }

    pkg.cantidad = document.getElementById("mdlCantidadPkg").value;

    params = {
        datosPaquete: JSON.stringify(pkg)
    };

    let ctype = 'application/x-www-form-urlencoded;charset=UTF-8';
    resp = await fetch(url, {
        method: "POST",
        headers: {'Content-Type': ctype},
        body: new URLSearchParams(params)
    });

    datos = await resp.json();

    if (datos.error != null) {
        Swal.fire('Error al guardar el paquete.', datos.error, 'warning');
        return;
    }
    if (datos.exception != null) {
        Swal.fire('', datos.exception, 'warning');
        return;
    }
    refreshTableGalletas();
    refreshTablePaquete();
    refreshTableInventario();
    Swal.fire('Movimiento Realizado',
            'Paquete Guardado correctamente.',
            'success');
}

function verificarAlertasInventario() {
    let alertaInventario = galletas.filter(galleta => galleta.cantidad <= 30);
    if (alertaInventario.length > 0) {
        alertaInventario.forEach(galleta => {
            Swal.fire('Advertencia',
                    `El producto "${galleta.sabor}" está en niveles bajos (${galleta.cantidad} unidades).`,
                    'warning');
        });
    }
}

function verificarAlertasPaquetes() {
    let alertaPaquetes = paquetes.filter(paquete => paquete.cantidad <= 2);
    if (alertaPaquetes.length > 0) {
        alertaPaquetes.forEach(paquete => {
            Swal.fire('Advertencia',
                    `El paquete con "${paquete.galleta.sabor}" está en niveles bajos (${paquete.cantidad} unidades).`,
                    'warning');
        });
    }
}
