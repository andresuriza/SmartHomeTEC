using Proyecto1.Models;

public class Dispositivo
{
    public int Id { get; set; }
    public string NumeroSerie { get; set; } // Único
    public string Marca { get; set; }
    public decimal ConsumoElectrico { get; set; } // Consumo en kWh
    public string Region { get; set; } // Nuevo campo para la región

    // Clave foránea obligatoria para relacionar con TipoDispositivo
    public int TipoDispositivoId { get; set; }
    public TipoDispositivo? TipoDispositivo { get; set; } = null;

    // Relaciones opcionales
    public ICollection<DispositivoUsuario> DispositivosUsuarios { get; set; } = new List<DispositivoUsuario>();
    public ICollection<Pedido> Pedidos { get; set; } = new List<Pedido>();
    public ICollection<Factura> Facturas { get; set; } = new List<Factura>();
}
