import React, { useState } from "react";
import * as XLSX from "xlsx";
import "./ManageStore.css";

function ManageStore() {
  const [products, setProducts] = useState([]);
  const [file, setFile] = useState(null);

  // Manejar la subida del archivo Excel
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setFile(file);
  };

  // Función para procesar el archivo Excel
  const handleFileUpload = () => {
    if (file) {
      const reader = new FileReader();

      reader.onload = (event) => {
        const data = new Uint8Array(event.target.result);
        const workbook = XLSX.read(data, { type: "array" });
        const sheetName = workbook.SheetNames[0]; // Leer la primera hoja
        const worksheet = workbook.Sheets[sheetName];
        const jsonData = XLSX.utils.sheet_to_json(worksheet);

        // Aquí almacenamos los productos leídos del archivo Excel
        setProducts(jsonData);
        alert("Archivo cargado correctamente y productos añadidos.");
      };

      reader.readAsArrayBuffer(file);
    } else {
      alert("Por favor, seleccione un archivo Excel.");
    }
  };

  return (
    <div className="manage-store-container">
      <h1>Gestionar la Tienda en Línea y Distribuidores</h1>

      <div className="file-upload">
        <label>Subir archivo Excel con productos por distribuidor:</label>
        <input type="file" accept=".xlsx, .xls" onChange={handleFileChange} />
        <button onClick={handleFileUpload}>Cargar Productos</button>
      </div>

      {products.length > 0 && (
        <div className="products-list">
          <h2>Productos Cargados</h2>
          <table>
            <thead>
              <tr>
                <th>Distribuidor</th>
                <th>Producto</th>
                <th>Marca</th>
                <th>Precio</th>
                <th>Región</th>
              </tr>
            </thead>
            <tbody>
              {products.map((product, index) => (
                <tr key={index}>
                  <td>{product.Distribuidor}</td>
                  <td>{product.Producto}</td>
                  <td>{product.Marca}</td>
                  <td>${product.Precio}</td>
                  <td>{product.Región}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default ManageStore;
