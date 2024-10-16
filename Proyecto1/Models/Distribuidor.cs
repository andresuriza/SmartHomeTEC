namespace Proyecto1.Models
{
    public class Distribuidor
    {
        public int Id { get; set; } // Nueva clave primaria autoincremental
        public string CedulaJuridica { get; set; } // Único
        public string Nombre { get; set; }
        public string Region { get; set; }

        // Relación con dispositivos
        public ICollection<Dispositivo> Dispositivos { get; set; }
    }

}
