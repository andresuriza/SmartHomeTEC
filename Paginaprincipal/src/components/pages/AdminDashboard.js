import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Importar useNavigate para redirigir
import './AdminDashboard.css';

export default function AdminDashboard() {
  // Hook para la navegación
  const navigate = useNavigate();

  // Estados para almacenar la lista de dispositivos, tipos de dispositivos, distribuidores y usuarios
  const [devices, setDevices] = useState([]);
  const [deviceTypes, setDeviceTypes] = useState([]);
  const [distributors, setDistributors] = useState([]);
  const [users, setUsers] = useState([
    { id: 1, name: 'Usuario 1', region: 'América', devices: [] },
    { id: 2, name: 'Usuario 2', region: 'Europa', devices: ['123456789'] } // Usuario con un dispositivo
  ]);

  const [newDevice, setNewDevice] = useState({
    type: '',
    serialNumber: '',
    brand: '',
    powerConsumption: '',
    region: ''
  });

  const [newDeviceType, setNewDeviceType] = useState({
    name: '',
    description: '',
    warrantyPeriod: ''
  });

  const [newDistributor, setNewDistributor] = useState({
    juridicalId: '',
    name: '',
    region: ''
  });

  const [errorMessage, setErrorMessage] = useState('');
  const [typeErrorMessage, setTypeErrorMessage] = useState('');
  const [distributorErrorMessage, setDistributorErrorMessage] = useState('');

  // Cálculo de indicadores
  const totalDevicesManaged = users.reduce((total, user) => total + user.devices.length, 0);
  const averageDevicesPerUser = users.length ? (totalDevicesManaged / users.length).toFixed(2) : 0;
  const devicesByRegion = devices.reduce((regions, device) => {
    if (!regions[device.region]) {
      regions[device.region] = 0;
    }
    regions[device.region]++;
    return regions;
  }, {});

  const deviceStatusList = devices.map((device) => {
    const isAssociatedWithUser = users.some((user) => user.devices.includes(device.serialNumber));
    return {
      ...device,
      status: isAssociatedWithUser ? 'Asociado a un cliente' : 'No asociado'
    };
  });

  // Función para agregar un nuevo dispositivo
  const handleAddDevice = (e) => {
    e.preventDefault();
    if (!newDevice.type || !newDevice.serialNumber || !newDevice.brand || !newDevice.powerConsumption || !newDevice.region) {
      setErrorMessage('Por favor, complete toda la información del dispositivo.');
      return;
    }

    const deviceExists = devices.some(device => device.serialNumber === newDevice.serialNumber);
    if (deviceExists) {
      setErrorMessage('No se puede agregar el dispositivo porque ya está asociado a un cliente.');
      return;
    }

    setDevices([...devices, newDevice]);
    setNewDevice({ type: '', serialNumber: '', brand: '', powerConsumption: '', region: '' });
    setErrorMessage('');
  };

  // Función para agregar un nuevo tipo de dispositivo
  const handleAddDeviceType = (e) => {
    e.preventDefault();
    if (!newDeviceType.name || !newDeviceType.description || !newDeviceType.warrantyPeriod) {
      setTypeErrorMessage('Por favor, complete toda la información del tipo de dispositivo.');
      return;
    }

    setDeviceTypes([...deviceTypes, newDeviceType]);
    setNewDeviceType({ name: '', description: '', warrantyPeriod: '' });
    setTypeErrorMessage('');
  };

  // Función para agregar un nuevo distribuidor
  const handleAddDistributor = (e) => {
    e.preventDefault();
    if (!newDistributor.juridicalId || !newDistributor.name || !newDistributor.region) {
      setDistributorErrorMessage('Por favor, complete toda la información del distribuidor.');
      return;
    }

    setDistributors([...distributors, newDistributor]);
    setNewDistributor({ juridicalId: '', name: '', region: '' });
    setDistributorErrorMessage('');
  };

  // Función para redirigir a la página de gestión de la tienda en línea
  const goToManageStore = () => {
    navigate("/gestionar-tienda");
  };

  return (
    <div className="dashboard-container">
      <h1 className="dashboard-title">Dashboard del Administrador</h1>

      {/* Sección de gestión de dispositivos */}
      <section className="section">
        <h2>Gestión de Dispositivos</h2>
        <p>Aquí puedes agregar dispositivos, pero no puedes editar aquellos que ya han sido asignados a un cliente.</p>

        <form className="device-form" onSubmit={handleAddDevice}>
          <div className="input-group">
            <label>Tipo de dispositivo</label>
            <input
              type="text"
              name="type"
              value={newDevice.type}
              onChange={(e) => setNewDevice({ ...newDevice, type: e.target.value })}
              placeholder="Ej: Refrigerador"
            />
          </div>

          <div className="input-group">
            <label>Número de serie</label>
            <input
              type="text"
              name="serialNumber"
              value={newDevice.serialNumber}
              onChange={(e) => setNewDevice({ ...newDevice, serialNumber: e.target.value })}
              placeholder="Número de serie"
            />
          </div>

          <div className="input-group">
            <label>Marca</label>
            <input
              type="text"
              name="brand"
              value={newDevice.brand}
              onChange={(e) => setNewDevice({ ...newDevice, brand: e.target.value })}
              placeholder="Ej: Samsung"
            />
          </div>

          <div className="input-group">
            <label>Consumo eléctrico (W)</label>
            <input
              type="text"
              name="powerConsumption"
              value={newDevice.powerConsumption}
              onChange={(e) => setNewDevice({ ...newDevice, powerConsumption: e.target.value })}
              placeholder="Ej: 500W"
            />
          </div>

          <div className="input-group">
            <label>Región</label>
            <input
              type="text"
              name="region"
              value={newDevice.region}
              onChange={(e) => setNewDevice({ ...newDevice, region: e.target.value })}
              placeholder="Ej: América"
            />
          </div>

          <button type="submit" className="btn">Agregar Dispositivo</button>
        </form>

        {errorMessage && <p className="error-message">{errorMessage}</p>}

        <h3>Dispositivos agregados:</h3>
        <ul>
          {devices.map((device, index) => (
            <li key={index}>
              <strong>{device.type}</strong> - {device.serialNumber} - {device.brand} - {device.powerConsumption}W - Región: {device.region}
            </li>
          ))}
        </ul>
      </section>

      {/* Sección de gestión de tipos de dispositivos */}
      <section className="section">
        <h2>Gestión de Tipos de Dispositivos</h2>
        <p>Administra los tipos de dispositivos y su información (nombre, descripción, tiempo de garantía).</p>

        <form className="device-type-form" onSubmit={handleAddDeviceType}>
          <div className="input-group">
            <label>Nombre del tipo de dispositivo</label>
            <input
              type="text"
              name="name"
              value={newDeviceType.name}
              onChange={(e) => setNewDeviceType({ ...newDeviceType, name: e.target.value })}
              placeholder="Ej: Electrodoméstico"
            />
          </div>

          <div className="input-group">
            <label>Descripción</label>
            <input
              type="text"
              name="description"
              value={newDeviceType.description}
              onChange={(e) => setNewDeviceType({ ...newDeviceType, description: e.target.value })}
              placeholder="Ej: Dispositivo que se usa en el hogar para tareas cotidianas."
            />
          </div>

          <div className="input-group">
            <label>Tiempo de garantía (años)</label>
            <input
              type="text"
              name="warrantyPeriod"
              value={newDeviceType.warrantyPeriod}
              onChange={(e) => setNewDeviceType({ ...newDeviceType, warrantyPeriod: e.target.value })}
              placeholder="Ej: 2 años"
            />
          </div>

          <button type="submit" className="btn">Agregar Tipo de Dispositivo</button>
        </form>

        {typeErrorMessage && <p className="error-message">{typeErrorMessage}</p>}

        <h3>Tipos de dispositivos agregados:</h3>
        <ul>
          {deviceTypes.map((type, index) => (
            <li key={index}>
              <strong>{type.name}</strong> - {type.description} - Garantía: {type.warrantyPeriod} años
            </li>
          ))}
        </ul>
      </section>

      {/* Sección de gestión de distribuidores */}
      <section className="section">
        <h2>Gestión de Distribuidores</h2>
        <p>Administra los distribuidores para ser utilizados en la tienda en línea (cédula jurídica, nombre, región).</p>

        <form className="distributor-form" onSubmit={handleAddDistributor}>
          <div className="input-group">
            <label>Cédula jurídica</label>
            <input
              type="text"
              name="juridicalId"
              value={newDistributor.juridicalId}
              onChange={(e) => setNewDistributor({ ...newDistributor, juridicalId: e.target.value })}
              placeholder="Ej: 123456789"
            />
          </div>

          <div className="input-group">
            <label>Nombre del distribuidor</label>
            <input
              type="text"
              name="name"
              value={newDistributor.name}
              onChange={(e) => setNewDistributor({ ...newDistributor, name: e.target.value })}
              placeholder="Ej: Distribuidor XYZ"
            />
          </div>

          <div className="input-group">
            <label>Región</label>
            <input
              type="text"
              name="region"
              value={newDistributor.region}
              onChange={(e) => setNewDistributor({ ...newDistributor, region: e.target.value })}
              placeholder="Ej: América, Europa"
            />
          </div>

          <button type="submit" className="btn">Agregar Distribuidor</button>
        </form>

        {distributorErrorMessage && <p className="error-message">{distributorErrorMessage}</p>}

        <h3>Distribuidores agregados:</h3>
        <ul>
          {distributors.map((distributor, index) => (
            <li key={index}>
              <strong>{distributor.name}</strong> - Cédula Jurídica: {distributor.juridicalId} - Región: {distributor.region}
            </li>
          ))}
        </ul>
      </section>

      {/* Indicadores clave */}
      <section className="section">
        <h2>Indicadores del Sistema</h2>
        <ul>
          <li>Cantidad promedio de dispositivos por usuario: {averageDevicesPerUser}</li>
          <li>Cantidad total de dispositivos gestionados por el sistema (asociados a un cliente): {totalDevicesManaged}</li>
          <li>Cantidad total de dispositivos por región:</li>
          <ul>
            {Object.entries(devicesByRegion).map(([region, count]) => (
              <li key={region}>{region}: {count} dispositivos</li>
            ))}
          </ul>
          <li>Listado de dispositivos registrados en el sistema:</li>
          <ul>
            {deviceStatusList.map((device, index) => (
              <li key={index}>
                <strong>{device.type}</strong> - {device.serialNumber} - {device.brand} - Estado: {device.status}
              </li>
            ))}
          </ul>
        </ul>
      </section>

      {/* Botón para ir a la gestión de la tienda en línea */}
      <section className="section">
        <h2>Gestión de la Tienda en Línea y Distribuidores</h2>
        <p>Administra los productos disponibles en la tienda en línea y asócialos con los distribuidores.</p>
        <button className="btn-manage-store" onClick={goToManageStore}>
          Ir a Gestión de la Tienda
        </button>
      </section>
    </div>
  );
}
