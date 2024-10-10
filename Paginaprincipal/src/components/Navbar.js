import React, { useState, useEffect } from "react";
import { Button } from "./Button";  // Importar el componente Button
import { Link } from "react-router-dom";
import "./Navbar.css";

function Navbar() {
  const [click, setClick] = useState(false);
  const [button, setButton] = useState(true);

  const handleClick = () => setClick(!click);
  const closeMobileMenu = () => setClick(false);

  const showButton = () => {
    if (window.innerWidth <= 960) {
      setButton(false);
    } else {
      setButton(true);
    }
  };

  useEffect(() => {
    showButton();
  }, []);

  window.addEventListener("resize", showButton);

  return (
    <>
      <nav className="navbar">
        <div className="navbar-container">
          <Link to="/" className="navbar-logo" onClick={closeMobileMenu}>
            SmartHomeTEC
            <i className="fa-solid fa-mobile-screen-button" />
          </Link>
          <div className="menu-icon" onClick={handleClick}>
            <i className={click ? "fas fa-times" : "fas fa-bars"} />
          </div>
          <ul className={click ? "nav-menu active" : "nav-menu"}>
            {/* Secciones de la navegación */}
            <li>
              <Link
                to="/login"
                className="nav-links-mobile"
                onClick={closeMobileMenu}
              >
                INICIA SESIÓN
              </Link>
            </li>
          </ul>
          {button && <Button buttonStyle="btn--outline">INICIA SESIÓN</Button>}
        </div>
      </nav>
    </>
  );
}

export default Navbar;
