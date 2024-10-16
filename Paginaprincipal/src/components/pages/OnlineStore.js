import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom'; 
import './OnlineStore.css';

function OnlineStore() {
  const location = useLocation();
  const userRegion = location.state?.userRegion || 'América'; 

  const [devices, setDevices] = useState([]);
  const [selectedDevice, setSelectedDevice] = useState(null);
  const [orderConfirmed, setOrderConfirmed] = useState(false);

  useEffect(() => {
    // Hacer la llamada a la API para obtener los dispositivos
    const fetchDevices = async () => {
      try {
        const response = await fetch('https://localhost:5555/api/Dispositivos');
        const data = await response.json();
        setDevices(data);
      } catch (error) {
        console.error('Error al obtener dispositivos:', error);
      }
    };

    fetchDevices();
  }, []);

  // Filtrar los dispositivos disponibles según la región del usuario
  const filteredDevices = devices.filter(
    (device) => device.region === userRegion
  );

  const handleDeviceSelect = (device) => {
    setSelectedDevice(device);
  };

  const handleOrderConfirm = () => {
    if (selectedDevice) {
      setOrderConfirmed(true);
      alert('Pedido confirmado. Se ha enviado la factura y certificado a su correo electrónico.');
    } else {
      alert('Por favor, seleccione un dispositivo.');
    }
  };

  return (
    <div className="store-container">
      <h1>Tienda en línea</h1>
      <p>Seleccione un dispositivo para comprar según su región: <strong>{userRegion}</strong></p>

      <div className="device-list">
        {filteredDevices.length > 0 ? (
          filteredDevices.map((device) => (
            <div
              key={device.id}
              className={`device-item ${selectedDevice && selectedDevice.id === device.id ? 'selected' : ''}`}
              onClick={() => handleDeviceSelect(device)}
            >
              <h3>{device.tipoDispositivo.nombre}</h3> {/* Mostrar el tipo de dispositivo */}
              <p>Marca: {device.marca}</p>
              <p>Precio: ${device.precio}</p> {/* Asumiendo que tienes un campo 'precio' */}
              <p>Garantía: {device.tipoDispositivo.tiempoGarantia} años</p> {/* Mostrar el tiempo de garantía del tipo de dispositivo */}
              <p>Número de serie: {device.numeroSerie}</p>
            </div>
          ))
        ) : (
          <p>No hay dispositivos disponibles para su región en este momento.</p>
        )}
      </div>

      <button onClick={handleOrderConfirm} className="btn-confirm">
        Confirmar Pedido
      </button>

      {orderConfirmed && selectedDevice && (
        <div className="order-details">
          <h2>Detalles del Pedido</h2>
          <p><strong>Dispositivo:</strong> {selectedDevice.tipoDispositivo.nombre}</p>
          <p><strong>Marca:</strong> {selectedDevice.marca}</p>
          <p><strong>Número de Serie:</strong> {selectedDevice.numeroSerie}</p>
          <p><strong>Precio:</strong> ${selectedDevice.precio}</p>
          <p><strong>Garantía:</strong> {selectedDevice.tipoDispositivo.tiempoGarantia} años</p>
        </div>
      )}
    </div>
  );
}

export default OnlineStore;
