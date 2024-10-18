import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import { Nav } from 'react-bootstrap';
import './AdminDashboard.css';

const Sidebar = ({ sections, activeSection, setActiveSection, isOpen, toggle }) => (
  <div className={`sidebar bg-light border-right ${isOpen ? 'is-open' : ''}`} style={{ minHeight: '100vh' }}>
    <div className="sidebar-header"></div>
    <Nav className="flex-column pt-2">
      {sections.map((section) => (
        <Nav.Item key={section} className={activeSection === section ? 'active' : ''}>
          <Nav.Link 
            as="button" 
            className="nav-link" 
            onClick={() => setActiveSection(section)}
          >
            <FontAwesomeIcon icon={faHome} className="mr-2" /> 
            {section}
          </Nav.Link>
        </Nav.Item>
      ))}
    </Nav>
  </div>
);

export default function AdminDashboard() {
  const navigate = useNavigate();
  const sections = [
    'Gestión de Dispositivos',
    'Gestión de Tipos de Dispositivos',
    'Gestión de Distribuidores',
    'Indicadores del Sistema',
    'Gestión de la Tienda en Línea'
  ];
  
  const [activeSection, setActiveSection] = useState(sections[0]);
  const [isOpen, setIsOpen] = useState(true);
  const [devices, setDevices] = useState([]);
  const [deviceTypes, setDeviceTypes] = useState([]);
  const [distributors, setDistributors] = useState([]);

  const [newDevice, setNewDevice] = useState({ id: '', type: '', serialNumber: '', brand: '', powerConsumption: '' });
  const [editing, setEditing] = useState(false);
  const [newDeviceType, setNewDeviceType] = useState({ id: '', name: '', description: '', warrantyPeriod: '' });
  const [newDistributor, setNewDistributor] = useState({ juridicalId: '', name: '', region: '' }); // Agregando región

  const [errorMessage, setErrorMessage] = useState('');
  const [typeErrorMessage, setTypeErrorMessage] = useState('');
  const [distributorErrorMessage, setDistributorErrorMessage] = useState('');

  const devicesByRegion = devices.reduce((regions, device) => {
    if (!regions[device.region]) {
      regions[device.region] = 0;
    }
    regions[device.region]++;
    return regions;
  }, {});

  // Fetch devices from the API
  const fetchDevices = async () => {
    try {
      const response = await fetch('https://localhost:5555/api/Dispositivos');
      const data = await response.json();
      setDevices(data);
    } catch (error) {
      console.error("Error fetching devices:", error);
    }
  };

  // Fetch device types from the API
  const fetchDeviceTypes = async () => {
    try {
      const response = await fetch('https://localhost:5555/api/TipoDispositivo');
      const data = await response.json();
      setDeviceTypes(data);
    } catch (error) {
      console.error("Error fetching device types:", error);
    }
  };

  // Fetch distributors from the API
  const fetchDistributors = async () => {
    try {
      const response = await fetch('https://localhost:5555/api/Distribuidor');
      const data = await response.json();
      setDistributors(data);
    } catch (error) {
      console.error("Error fetching distributors:", error);
    }
  };

  useEffect(() => {
    fetchDevices();
    fetchDeviceTypes(); 
    fetchDistributors(); // Fetch distributors when component mounts
  }, []);

  const handleAddDevice = async (e) => {
    e.preventDefault();
    if (!newDevice.type || !newDevice.serialNumber || !newDevice.brand || !newDevice.powerConsumption) {
      setErrorMessage('Por favor, complete toda la información del dispositivo.');
      return;
    }

    const deviceExists = devices.some(device => device.numeroSerie === newDevice.serialNumber);
    if (deviceExists) {
      setErrorMessage('No se puede agregar el dispositivo porque ya está asociado a un cliente.');
      return;
    }

    const deviceToAdd = {
      NumeroSerie: newDevice.serialNumber,
      Marca: newDevice.brand,
      ConsumoElectrico: newDevice.powerConsumption,
      TipoDispositivoId: newDevice.type 
    };

    try {
      const response = await fetch('https://localhost:5555/api/Dispositivos', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(deviceToAdd),
      });

      if (!response.ok) {
        throw new Error('Error al agregar el dispositivo');
      }

      // Reset the form and refresh the devices list
      setNewDevice({ id: '', type: '', serialNumber: '', brand: '', powerConsumption: '' });
      setEditing(false);
      setErrorMessage('');
      await fetchDevices(); // Refresh the device list
    } catch (error) {
      setErrorMessage('Error al agregar el dispositivo: ' + error.message);
    }
  };

  const handleEditDevice = (device) => {
    setNewDevice({
      id: device.id,
      type: device.tipoDispositivo.id,
      serialNumber: device.numeroSerie,
      brand: device.marca,
      powerConsumption: device.consumoElectrico
    });
    setEditing(true); // Cambia a modo edición
  };

  const handleUpdateDevice = async (e) => {
    e.preventDefault();
    if (!newDevice.type || !newDevice.serialNumber || !newDevice.brand || !newDevice.powerConsumption) {
      setErrorMessage('Por favor, complete toda la información del dispositivo.');
      return;
    }

    const deviceToUpdate = {
      NumeroSerie: newDevice.serialNumber,
      Marca: newDevice.brand,
      ConsumoElectrico: newDevice.powerConsumption,
      TipoDispositivoId: newDevice.type
    };

    try {
      const response = await fetch(`https://localhost:5555/api/Dispositivos/${newDevice.serialNumber}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(deviceToUpdate),
      });

      if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(errorResponse.message || 'Error al actualizar el dispositivo');
      }

      // Reset the form and refresh the devices list
      setNewDevice({ id: '', type: '', serialNumber: '', brand: '', powerConsumption: '' });
      setEditing(false);
      setErrorMessage('');
      await fetchDevices(); // Refresh the device list
    } catch (error) {
      setErrorMessage('Error al actualizar el dispositivo: ' + error.message);
    }
  };

  // Funciones para gestionar tipos de dispositivos
  const handleAddDeviceType = async (e) => {
    e.preventDefault();
    if (!newDeviceType.name || !newDeviceType.description || !newDeviceType.warrantyPeriod) {
      setTypeErrorMessage('Por favor, complete toda la información del tipo de dispositivo.');
      return;
    }

    const deviceTypeToAdd = {
      nombre: newDeviceType.name,
      descripcion: newDeviceType.description,
      tiempoGarantia: newDeviceType.warrantyPeriod,
    };

    try {
      const response = await fetch('https://localhost:5555/api/TipoDispositivo', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(deviceTypeToAdd),
      });

      if (!response.ok) {
        throw new Error('Error al agregar el tipo de dispositivo');
      }

      const addedDeviceType = await response.json();
      setDeviceTypes([...deviceTypes, addedDeviceType]);
      setNewDeviceType({ id: '', name: '', description: '', warrantyPeriod: '' });
      setTypeErrorMessage('');
      await fetchDeviceTypes(); // Refresh the device types list
    } catch (error) {
      setTypeErrorMessage('Error al agregar el tipo de dispositivo: ' + error.message);
    }
  };

  // Function to handle editing a device type
  const handleEditDeviceType = (deviceType) => {
    setNewDeviceType({
      id: deviceType.id,
      name: deviceType.nombre,
      description: deviceType.descripcion,
      warrantyPeriod: deviceType.tiempoGarantia
    });
    setEditing(true); // Switch to editing mode
  };

  const handleUpdateDeviceType = async (e) => {
    e.preventDefault();
    if (!newDeviceType.name || !newDeviceType.description || !newDeviceType.warrantyPeriod) {
      setTypeErrorMessage('Por favor, complete toda la información del tipo de dispositivo.');
      return;
    }

    const deviceTypeToEdit = {
      id: newDeviceType.id,
      nombre: newDeviceType.name,
      descripcion: newDeviceType.description,
      tiempoGarantia: newDeviceType.warrantyPeriod,
    };

    try {
      const response = await fetch(`https://localhost:5555/api/TipoDispositivo/${newDeviceType.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(deviceTypeToEdit),
      });

      if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(errorResponse.message || 'Error al actualizar el tipo de dispositivo');
      }

      // Reset the form and refresh the device types list
      setNewDeviceType({ id: '', name: '', description: '', warrantyPeriod: '' });
      setEditing(false);
      setTypeErrorMessage('');
      await fetchDeviceTypes(); // Refresh the device types list
    } catch (error) {
      setTypeErrorMessage('Error al actualizar el tipo de dispositivo: ' + error.message);
    }
  };

  // Funciones para gestionar distribuidores
  const handleAddDistributor = async (e) => {
    e.preventDefault();
    if (!newDistributor.juridicalId || !newDistributor.name || !newDistributor.region) { // Verifica la región
      setDistributorErrorMessage('Por favor, complete toda la información del distribuidor.');
      return;
    }

    const distributorToAdd = {
      CedulaJuridica: newDistributor.juridicalId,
      Nombre: newDistributor.name,
      Region: newDistributor.region, // Agregando la región
    };

    try {
      const response = await fetch('https://localhost:5555/api/Distribuidor', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(distributorToAdd),
      });

      if (!response.ok) {
        throw new Error('Error al agregar el distribuidor');
      }

      await fetchDistributors(); // Refresh the distributors list
      setNewDistributor({ juridicalId: '', name: '', region: '' }); // Reinicia el formulario
      setDistributorErrorMessage('');
    } catch (error) {
      setDistributorErrorMessage('Error al agregar el distribuidor: ' + error.message);
    }
  };

  const handleEditDistributor = (distributor) => {
    setNewDistributor({
      juridicalId: distributor.cedulaJuridica,
      name: distributor.nombre,
      region: distributor.region, // Agregando la región al editar
    });
    setEditing(true); // Cambia a modo edición
  };

  const handleUpdateDistributor = async (e) => {
    e.preventDefault();
    if (!newDistributor.juridicalId || !newDistributor.name || !newDistributor.region) { // Verifica la región
      setDistributorErrorMessage('Por favor, complete toda la información del distribuidor.');
      return;
    }

    const distributorToUpdate = {
      CedulaJuridica: newDistributor.juridicalId,
      Nombre: newDistributor.name,
      Region: newDistributor.region, // Agregando la región
    };

    try {
      const response = await fetch(`https://localhost:5555/api/Distribuidor/${newDistributor.juridicalId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(distributorToUpdate),
      });

      if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(errorResponse.message || 'Error al actualizar el distribuidor');
      }

      await fetchDistributors(); // Refresh the distributors list
      setNewDistributor({ juridicalId: '', name: '', region: '' }); // Reinicia el formulario
      setDistributorErrorMessage('');
      setEditing(false);
    } catch (error) {
      setDistributorErrorMessage('Error al actualizar el distribuidor: ' + error.message);
    }
  };

  const goToManageStore = () => {
    navigate("/gestionar-tienda");
  };

  const toggleSidebar = () => {
    setIsOpen(!isOpen);
  };

  const handleCancelEdit = () => {
    setNewDevice({ id: '', type: '', serialNumber: '', brand: '', powerConsumption: '' });
    setNewDeviceType({ id: '', name: '', description: '', warrantyPeriod: '' });
    setNewDistributor({ juridicalId: '', name: '', region: '' }); // Reinicia el formulario de distribuidor
    setEditing(false); // Cambia a modo agregar
    setErrorMessage('');
    setTypeErrorMessage('');
    setDistributorErrorMessage('');
  };

  return (
    <div className="dashboard-container d-flex">
      <Sidebar 
        sections={sections} 
        activeSection={activeSection} 
        setActiveSection={setActiveSection} 
        isOpen={isOpen} 
        toggle={toggleSidebar} 
      />

      <div className="content p-4">
        <h1 className="dashboard-title">Dashboard del Administrador</h1>

        {/* Contenido basado en la sección activa */}
        {activeSection === 'Gestión de Dispositivos' && (
          <section className="section">
            <h2>Gestión de Dispositivos</h2>
            <p>Aquí puedes agregar dispositivos, pero no puedes editar aquellos que ya han sido asignados a un cliente.</p>

            <form className="device-form" onSubmit={editing ? handleUpdateDevice : handleAddDevice}>
              <div className="form-group">
                <label>Tipo de dispositivo</label>
                <select
                  className="form-control"
                  value={newDevice.type}
                  onChange={(e) => setNewDevice({ ...newDevice, type: e.target.value })}
                >
                  <option value="">Selecciona un tipo de dispositivo</option>
                  {deviceTypes.map((deviceType) => (
                    <option key={deviceType.id} value={deviceType.id}>
                      {deviceType.nombre} (ID: {deviceType.id})
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label>Número de serie</label>
                <input
                  type="text"
                  className="form-control"
                  value={newDevice.serialNumber}
                  onChange={(e) => setNewDevice({ ...newDevice, serialNumber: e.target.value })}
                  readOnly={editing}
                />
              </div>

              <div className="form-group">
                <label>Marca</label>
                <input
                  type="text"
                  className="form-control"
                  value={newDevice.brand}
                  onChange={(e) => setNewDevice({ ...newDevice, brand: e.target.value })}
                  placeholder="Ej: Samsung"
                />
              </div>

              <div className="form-group">
                <label>Consumo eléctrico (W)</label>
                <input
                  type="text"
                  className="form-control"
                  value={newDevice.powerConsumption}
                  onChange={(e) => setNewDevice({ ...newDevice, powerConsumption: e.target.value })}
                  placeholder="Ej: 500W"
                />
              </div>

              <div className="d-flex justify-content-between mt-3">
                <button type="submit" className="btn btn-primary">
                  {editing ? 'Guardar Edición' : 'Agregar Dispositivo'}
                </button>
                {editing && (
                  <button type="button" className="btn btn-secondary" onClick={handleCancelEdit}>
                    Cancelar
                  </button>
                )}
              </div>
            </form>

            {errorMessage && <p className="text-danger">{errorMessage}</p>}

            <h3>Lista de Dispositivos:</h3>
            <div style={{ maxHeight: '300px', overflowY: 'scroll' }}>
              <table className="table table-striped">
                <thead>
                  <tr>
                    <th>Número de Serie</th>
                    <th>Marca</th>
                    <th>Consumo Eléctrico (W)</th>
                    <th>Tipo de Dispositivo</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {devices.map((device) => (
                    <tr key={device.numeroSerie}>
                      <td>{device.numeroSerie}</td>
                      <td>{device.marca}</td>
                      <td>{device.consumoElectrico}</td>
                      <td>{device.tipoDispositivo?.nombre}</td>
                      <td>
                        <button 
                          className="btn btn-warning"
                          onClick={() => handleEditDevice(device)}
                        >
                          Editar
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        )}

        {/* Sección para gestionar tipos de dispositivos */}
        {activeSection === 'Gestión de Tipos de Dispositivos' && (
          <section className="section">
            <h2>Gestión de Tipos de Dispositivos</h2>
            <p>Aquí puedes agregar y gestionar tipos de dispositivos.</p>

            <form className="device-type-form" onSubmit={editing ? handleUpdateDeviceType : handleAddDeviceType}>
              <div className="form-group">
                <label>Nombre del tipo de dispositivo</label>
                <input
                  type="text"
                  className="form-control"
                  value={newDeviceType.name}
                  onChange={(e) => setNewDeviceType({ ...newDeviceType, name: e.target.value })}
                  placeholder="Ej: Electrodoméstico"
                />
              </div>

              <div className="form-group">
                <label>Descripción</label>
                <input
                  type="text"
                  className="form-control"
                  value={newDeviceType.description}
                  onChange={(e) => setNewDeviceType({ ...newDeviceType, description: e.target.value })}
                  placeholder="Ej: Dispositivo que se usa en el hogar para tareas cotidianas."
                />
              </div>

              <div className="form-group">
                <label>Tiempo de garantía (años)</label>
                <input
                  type="text"
                  className="form-control"
                  value={newDeviceType.warrantyPeriod}
                  onChange={(e) => setNewDeviceType({ ...newDeviceType, warrantyPeriod: e.target.value })}
                  placeholder="Ej: 2 años"
                />
              </div>

              <div className="d-flex justify-content-between mt-3">
                <button type="submit" className="btn btn-primary">
                  {editing ? 'Guardar Edición' : 'Agregar Tipo de Dispositivo'}
                </button>
                {editing && (
                  <button type="button" className="btn btn-secondary" onClick={handleCancelEdit}>
                    Cancelar
                  </button>
                )}
              </div>
            </form>

            {typeErrorMessage && <p className="text-danger">{typeErrorMessage}</p>}

            <h3>Tipos de dispositivos agregados:</h3>
            <div style={{ maxHeight: '300px', overflowY: 'scroll' }}>
              <table className="table table-striped">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Descripción</th>
                    <th>Tiempo de Garantía (años)</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {deviceTypes.map((deviceType) => (
                    <tr key={deviceType.id}>
                      <td>{deviceType.id}</td>
                      <td>{deviceType.nombre}</td>
                      <td>{deviceType.descripcion}</td>
                      <td>{deviceType.tiempoGarantia}</td>
                      <td>
                        <button 
                          className="btn btn-warning"
                          onClick={() => handleEditDeviceType(deviceType)}
                        >
                          Editar
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        )}

        {/* Gestión de Distribuidores */}
        {activeSection === 'Gestión de Distribuidores' && (
          <section className="section">
            <h2>Gestión de Distribuidores</h2>
            <p>Administra los distribuidores para ser utilizados en la tienda en línea (cédula jurídica, nombre, región).</p>

            <form className="distributor-form" onSubmit={editing ? handleUpdateDistributor : handleAddDistributor}>
              <div className="form-group">
                <label>Cédula jurídica</label>
                <input
                  type="text"
                  className="form-control"
                  value={newDistributor.juridicalId}
                  onChange={(e) => setNewDistributor({ ...newDistributor, juridicalId: e.target.value })}
                  placeholder="Ej: 123456789"
                />
              </div>

              <div className="form-group">
                <label>Nombre del distribuidor</label>
                <input
                  type="text"
                  className="form-control"
                  value={newDistributor.name}
                  onChange={(e) => setNewDistributor({ ...newDistributor, name: e.target.value })}
                  placeholder="Ej: Distribuidor XYZ"
                />
              </div>

              <div className="form-group">
                <label>Región</label>
                <input
                  type="text"
                  className="form-control"
                  value={newDistributor.region}
                  onChange={(e) => setNewDistributor({ ...newDistributor, region: e.target.value })}
                  placeholder="Ej: San José"
                />
              </div>

              <div className="d-flex justify-content-between mt-3">
                <button type="submit" className="btn btn-primary">
                  {editing ? 'Guardar Edición' : 'Agregar Distribuidor'}
                </button>
                {editing && (
                  <button type="button" className="btn btn-secondary" onClick={handleCancelEdit}>
                    Cancelar
                  </button>
                )}
              </div>
            </form>

            {distributorErrorMessage && <p className="text-danger">{distributorErrorMessage}</p>}

            <h3>Distribuidores agregados:</h3>
            <div style={{ maxHeight: '300px', overflowY: 'scroll' }}>
              <table className="table table-striped">
                <thead>
                  <tr>
                    <th>Cédula Jurídica</th>
                    <th>Nombre</th>
                    <th>Región</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {distributors.map((distributor) => (
                    <tr key={distributor.cedulaJuridica}>
                      <td>{distributor.cedulaJuridica}</td>
                      <td>{distributor.nombre}</td>
                      <td>{distributor.region}</td> {/* Mostrando la región */}
                      <td>
                        <button 
                          className="btn btn-warning"
                          onClick={() => handleEditDistributor(distributor)}
                        >
                          Editar
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        )}

        {activeSection === 'Indicadores del Sistema' && (
          <section className="section">
            <h2>Indicadores del Sistema</h2>
            <ul className="list-group">
              <li className="list-group-item">Cantidad total de dispositivos gestionados por el sistema: {devices.length}</li>
              <li className="list-group-item">Cantidad total de dispositivos por región:</li>
              <ul className="list-group">
                {Object.entries(devicesByRegion).map(([region, count]) => (
                  <li key={region} className="list-group-item">{region}: {count} dispositivos</li>
                ))}
              </ul>
            </ul>
          </section>
        )}

        {activeSection === 'Gestión de la Tienda en Línea' && (
          <section className="section">
            <h2>Gestión de la Tienda en Línea y Distribuidores</h2>
            <p>Administra los productos disponibles en la tienda en línea y asócialos con los distribuidores.</p>
            <button className="btn btn-success" onClick={goToManageStore}>
              Ir a Gestión de la Tienda
            </button>
          </section>
        )}
      </div>
    </div>
  );
}
