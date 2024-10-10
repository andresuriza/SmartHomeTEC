import React from 'react';
import './AdminDashboard.css';

export default function AdminDashboard() {
  return (
    <div className="dashboard-container">
      <h1 className="dashboard-title">Dashboard del Administrador</h1>
      
      {/* Sección de gestión de dispositivos */}
      <section className="section">
        <h2>Gestión de Dispositivos</h2>
        <p>Aquí puedes agregar, editar o eliminar dispositivos.</p>
        <button className="btn">Agregar Dispositivo</button>
        <button className="btn">Gestionar Dispositivos</button>
      </section>

      {/* Sección de gestión de tipos de dispositivos */}
      <section className="section">
        <h2>Gestión de Tipos de Dispositivos</h2>
        <p>Administra los tipos de dispositivos y su información.</p>
        <button className="btn">Agregar Tipo de Dispositivo</button>
        <button className="btn">Gestionar Tipos de Dispositivos</button>
      </section>

      {/* Sección de gestión de distribuidores */}
      <section className="section">
        <h2>Gestión de Distribuidores</h2>
        <p>Administra los distribuidores que forman parte del sistema.</p>
        <button className="btn">Agregar Distribuidor</button>
        <button className="btn">Gestionar Distribuidores</button>
      </section>

      {/* Indicadores clave */}
      <section className="section">
        <h2>Indicadores del Sistema</h2>
        <ul>
          <li>Cantidad promedio de dispositivos por usuarios</li>
          <li>Cantidad total de dispositivos gestionados por el sistema</li>
          <li>Cantidad total de dispositivos por región</li>
          <li>Listado de dispositivos no registrados</li>
        </ul>
      </section>
    </div>
  );
}
