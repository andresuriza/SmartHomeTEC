import React, { useState, useEffect } from 'react';
import "../../App.css";
import { useAuth } from "../../AuthContext";
import "./ProfileManagement.css";

export default function ProfileManagement() {
  const { user } = useAuth();
  const [profile, setProfile] = useState({
    nombre: "",
    apellidos: "",
    email: "",
    region: "",
    direccionesEntrega: []
  });
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        if (user && user.userId) {
          const response = await fetch(`https://localhost:5555/api/users/${user.userId}/details`, {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${user.token}`,
            }
          });

          const data = await response.json();

          if (response.ok) {
            setProfile({
              nombre: data.nombre,
              apellidos: data.apellidos,
              email: data.correoElectronico,
              region: data.region,
              direccionesEntrega: data.direccionesEntrega
            });
            setLoading(false);
          } else {
            setErrorMessage(data.message || "Error al obtener los datos del usuario");
            setLoading(false);
          }
        }
      } catch (error) {
        console.error("Error al conectarse con la API:", error);
        setErrorMessage("Error al conectarse con la API");
        setLoading(false);
      }
    };

    fetchUserData();
  }, [user]);

  const handleProfileChange = (e) => {
    setProfile({
      ...profile,
      [e.target.name]: e.target.value,
    });
  };

  const handleProfileSubmit = (e) => {
    e.preventDefault();
    // Aquí puedes enviar los datos actualizados al servidor si es necesario
    alert("Perfil actualizado con éxito");
  };

  if (loading) {
    return <div>Cargando...</div>;
  }

  return (
    <div className="profile-container">
      <h1>Gestión del Perfil</h1>

      {errorMessage && <p className="error-message">{errorMessage}</p>}

      <div className="profile-info">
        <p><strong>Nombre:</strong> {profile.nombre}</p>
        <p><strong>Apellidos:</strong> {profile.apellidos}</p>
        <p><strong>Email:</strong> {profile.email}</p>
        <p><strong>Región:</strong> {profile.region}</p>
        <p><strong>Direcciones de Entrega:</strong></p>
        <ul>
          {profile.direccionesEntrega.map((direccion, index) => (
            <li key={index}>
              {direccion.calle}, {direccion.ciudad}, {direccion.codigoPostal}, {direccion.pais}
            </li>
          ))}
        </ul>
      </div>

      <form onSubmit={handleProfileSubmit}>
        <div className="input-group">
          <label htmlFor="nombre">Nombre</label>
          <input
            id="nombre"
            name="nombre"
            type="text"
            value={profile.nombre}
            onChange={handleProfileChange}
          />
        </div>
        <div className="input-group">
          <label htmlFor="apellidos">Apellidos</label>
          <input
            id="apellidos"
            name="apellidos"
            type="text"
            value={profile.apellidos}
            onChange={handleProfileChange}
          />
        </div>
        <div className="input-group">
          <label htmlFor="email">Email</label>
          <input
            id="email"
            name="email"
            type="email"
            value={profile.email}
            disabled
          />
        </div>
        <div className="input-group">
          <label htmlFor="region">Región</label>
          <input
            id="region"
            name="region"
            type="text"
            value={profile.region}
            onChange={handleProfileChange}
          />
        </div>
        <div className="button-container">
          <button type="submit" className="btn">
            Guardar Cambios
          </button>
        </div>
      </form>
    </div>
  );
}
