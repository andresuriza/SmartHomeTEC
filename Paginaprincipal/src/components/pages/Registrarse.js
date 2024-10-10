import React, { useState } from "react";
import "../../App.css";
import { useNavigate } from "react-router-dom";
import "./Registrarse.css"; // Asegúrate de crear este archivo CSS

export default function Registrarse() {
  const navigate = useNavigate();

  const handleClick = () => {
    // Aquí puedes manejar el registro o enviar los datos a la API antes de navegar
    navigate("/"); // Redirigir después del registro
  };

  return (
    <div className="register-container">
      <h1 className="registrarse">¡Únete a Smart Home!</h1>
      <p className="description">Regístrate y empieza a controlar tu hogar de manera inteligente.</p>
      <form className="register-form">
        
        {/* Nombre */}
        <div className="input-group">
          <label>Nombre</label>
          <input id="nombre" type="text" placeholder="Ingresa tu nombre" />
        </div>

        {/* Primer Apellido */}
        <div className="input-group">
          <label>Primer Apellido</label>
          <input id="apellido1" type="text" placeholder="Ingresa tu primer apellido" />
        </div>

        {/* Segundo Apellido */}
        <div className="input-group">
          <label>Segundo Apellido</label>
          <input id="apellido2" type="text" placeholder="Ingresa tu segundo apellido" />
        </div>

        {/* Región - Continente */}
        <div className="input-group">
          <label>Continente</label>
          <select id="continente">
            <option value="america">América</option>
            <option value="europa">Europa</option>
            <option value="asia">Asia</option>
            <option value="africa">África</option>
            <option value="oceania">Oceanía</option>
          </select>
        </div>

        {/* Región - País */}
        <div className="input-group">
          <label>País</label>
          <input id="pais" type="text" placeholder="Ingresa tu país" />
        </div>

        {/* Correo Electrónico */}
        <div className="input-group">
          <label htmlFor="email">Email</label>
          <input id="email" type="email" placeholder="Ingresa tu correo electrónico" />
        </div>

        {/* Contraseña */}
        <div className="input-group">
          <label htmlFor="password">Contraseña</label>
          <input id="password" type="password" placeholder="Ingresa tu contraseña" />
        </div>

        {/* Dirección de Entrega */}
        <div className="input-group">
          <label>Dirección de Entrega</label>
          <input id="direccion" type="text" placeholder="Ingresa tu dirección de entrega" />
        </div>

        <button type="button" onClick={handleClick} className="btn-register">
          Registrarse
        </button>

        <p className="message">¡Estás a solo un paso de controlar tu hogar con Smart Home!</p>
      </form>
    </div>
  );
}
