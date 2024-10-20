using System.ComponentModel.DataAnnotations;

namespace Proyecto1.Models
{
    public class Administrador
    {
        [Key]
        public int Id { get; set; }
        public string Email { get; set; }

        public string Contrasena { get; set; }
    }
}
