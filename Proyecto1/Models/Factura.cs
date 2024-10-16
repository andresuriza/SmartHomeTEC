namespace Proyecto1.Models
{
    using System.ComponentModel.DataAnnotations; // Importa esta librería si aún no lo has hecho

    public class Factura
    {
        [Key]  // Marca NumeroFactura como clave primaria
        public int NumeroFactura { get; set; } // Clave primaria
        public DateTime FechaCompra { get; set; }

        public int UsuarioId { get; set; }
        public User Usuario { get; set; }

        public int DispositivoId { get; set; }
        public Dispositivo Dispositivo { get; set; }

        public decimal Precio { get; set; }
    }


}
