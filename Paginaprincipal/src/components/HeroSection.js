import React from "react";
import "../App.css";
import "./HeroSection.css";
import { Button } from "./Button";
import { Link } from "react-router-dom";

// Importa las imágenes locales (asegúrate de agregar imágenes relacionadas a dispositivos inteligentes)
import smartHomeImage1 from "../images/casa.png";
import smartHomeImage2 from "../images/berries.png";

function HeroSection() {
  return (
    <div className="hero-container">
      {/* Bienvenida */}
      <div className="welcome-message">
        <h2>¡Bienvenido a SmartHomeTEC!</h2>
        <p>La plataforma que te permitirá gestionar todos tus dispositivos inteligentes desde un solo lugar.</p>
      </div>

      {/* Texto principal del Hero */}
      <div className="hero-text">
        <h1>Controla tu hogar inteligente de manera eficiente.</h1>
        <p>
          ¿Quieres administrar tus dispositivos de forma centralizada? Monitoriza y controla todos tus dispositivos con SmartHomeTEC.
        </p>
        <div className="hero-btns">
          <Button className="btns" buttonStyle="btn--primary" buttonSize="btn--large">
            EMPIEZA AHORA
          </Button>
        </div>
      </div>

      {/* Imágenes de apoyo */}
      <div className="hero-images">
        {/* Reemplaza las imágenes remotas por las locales */}
        <img src={smartHomeImage1} alt="Dispositivo inteligente 2" className="hero-image" />
      </div>
    </div>
  );
}

export default HeroSection;
