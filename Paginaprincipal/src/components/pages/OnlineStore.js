import React, { useState } from 'react';
import { useLocation } from 'react-router-dom'; // Importar useLocation para obtener el estado
import './OnlineStore.css';

const availableDevices = [
  {
    id: 1,
    type: 'Refrigerador',
    brand: 'Samsung',
    serialNumber: 'RF123456',
    region: 'América',
    price: 800,
    warrantyPeriod: 2 // años
  },
  {
    id: 2,
    type: 'Televisor',
    brand: 'LG',
    serialNumber: 'TV789012',
    region: 'Europa',
    price: 600,
    warrantyPeriod: 3 // años
  },
  {
    id: 3,
    type: 'Lavadora',
    brand: 'Whirlpool',
    serialNumber: 'LW345678',
    region: 'América',
    price: 400,
    warrantyPeriod: 2 // años
  },
  {
    id: 4,
    type: 'Aire Acondicionado',
    brand: 'Daikin',
    serialNumber: 'AC910111',
    region: 'Asia',
    price: 500,
    warrantyPeriod: 1 // años
  }
];

function OnlineStore() {
  const location = useLocation(); // Obtener la región del estado pasado por ClientView
  const userRegion = location.state?.userRegion || 'América'; // Región del usuario (por defecto América si no se pasa ninguna)

  const [selectedDevice, setSelectedDevice] = useState(null);
  const [orderConfirmed, setOrderConfirmed] = useState(false);

  // Filtrar los dispositivos disponibles según la región del usuario
  const filteredDevices = availableDevices.filter(
    (device) => device.region === userRegion
  );

  const handleDeviceSelect = (device) => {
    setSelectedDevice(device);
  };

  const handleOrderConfirm = () => {
    if (selectedDevice) {
      setOrderConfirmed(true);
      alert('Pedido confirmado. Se ha enviado la factura y certificado a su correo electrónico.');
      // Aquí podrías generar los PDFs de la factura y garantía y enviar el correo electrónico.
    } else {
      alert('Por favor, seleccione un dispositivo.');
    }
  };

  return (
    <div className="store-container">
      <h1>Tienda en línea</h1>
      <p>Seleccione un dispositivo para comprar según su región: <strong>{userRegion}</strong></p>

      {/* Mostrar dispositivos disponibles */}
      <div className="device-list">
        {filteredDevices.length > 0 ? (
          filteredDevices.map((device) => (
            <div
              key={device.id}
              className={`device-item ${selectedDevice && selectedDevice.id === device.id ? 'selected' : ''}`}
              onClick={() => handleDeviceSelect(device)}
            >
              <h3>{device.type}</h3>
              <p>Marca: {device.brand}</p>
              <p>Precio: ${device.price}</p>
              <p>Garantía: {device.warrantyPeriod} años</p>
              <p>Número de serie: {device.serialNumber}</p>
            </div>
          ))
        ) : (
          <p>No hay dispositivos disponibles para su región en este momento.</p>
        )}
      </div>

      {/* Botón para confirmar el pedido */}
      <button onClick={handleOrderConfirm} className="btn-confirm">
        Confirmar Pedido
      </button>

      {/* Mostrar mensaje de confirmación */}
      {orderConfirmed && selectedDevice && (
        <div className="order-details">
          <h2>Detalles del Pedido</h2>
          <p><strong>Dispositivo:</strong> {selectedDevice.type}</p>
          <p><strong>Marca:</strong> {selectedDevice.brand}</p>
          <p><strong>Número de Serie:</strong> {selectedDevice.serialNumber}</p>
          <p><strong>Precio:</strong> ${selectedDevice.price}</p>
          <p><strong>Garantía:</strong> {selectedDevice.warrantyPeriod} años</p>
        </div>
      )}
    </div>
  );
}

export default OnlineStore;


