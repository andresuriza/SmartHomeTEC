using Proyecto1.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Proyecto1.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private readonly SmartHomeDbContext _context;

        public UsersController(SmartHomeDbContext context)
        {
            _context = context;
        }

        // GET: api/Users
        [HttpGet]
        public async Task<ActionResult<IEnumerable<User>>> GetUsers()
        {
            return await _context.Users.ToListAsync();
        }

        // GET: api/Users/{id}
        [HttpGet("{id}")]
        public async Task<ActionResult<User>> GetUser(int id)
        {
            var user = await _context.Users.FindAsync(id);

            if (user == null)
            {
                return NotFound();
            }

            return user;
        }

        // GET: api/Users/{id}/details
        [HttpGet("{id}/details")]
        public async Task<ActionResult<UserDetailsDto>> GetUserDetails(int id)
        {
            var user = await _context.Users
                .Include(u => u.DireccionesEntrega) // Incluir las direcciones de entrega
                .Where(u => u.Id == id)
                .Select(u => new UserDetailsDto
                {
                    Nombre = u.Nombre,
                    Apellidos = u.Apellidos,
                    Region = u.Region,
                    CorreoElectronico = u.CorreoElectronico,
                    DireccionesEntrega = u.DireccionesEntrega.Select(d => new DireccionEntregaDto
                    {
                        Id = d.Id,
                        Calle = d.Calle,
                        Ciudad = d.Ciudad,
                        CodigoPostal = d.CodigoPostal,
                        Pais = d.Pais
                    }).ToList()
                })
                .FirstOrDefaultAsync();

            if (user == null)
            {
                return NotFound();
            }

            return Ok(user);
        }

        // PUT: api/Users/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> PutUser(int id, User user)
        {
            if (id != user.Id)
            {
                return BadRequest();
            }

            _context.Entry(user).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/Users/register
        [Route("register")]
        [HttpPost]
        public async Task<ActionResult<User>> PostUser(User user)
        {
            // Verificar si el correo ya existe
            if (_context.Users.Any(u => u.CorreoElectronico == user.CorreoElectronico))
            {
                return Conflict(new { message = "El correo electrónico ya está en uso." });
            }

            // Agregar el usuario a la base de datos
            _context.Users.Add(user);
            await _context.SaveChangesAsync();

            // Asignar el UserId a cada dirección de entrega asociada
            if (user.DireccionesEntrega != null)
            {
                foreach (var direccion in user.DireccionesEntrega)
                {
                    direccion.UserId = user.Id;
                    _context.DireccionesEntrega.Add(direccion);
                }

                await _context.SaveChangesAsync(); // Guardar las direcciones con el UserId asociado
            }

            return CreatedAtAction("GetUser", new { id = user.Id }, user);
        }

        // POST: api/Users/login
        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginDto loginDto)
        {
            // Buscar usuario con el correo electrónico y la contraseña proporcionados
            var user = await _context.Users
                .Where(u => u.CorreoElectronico == loginDto.Email && u.Contrasena == loginDto.Password)
                .FirstOrDefaultAsync();

            if (user == null)
            {
                return Unauthorized(new { message = "Email o contraseña incorrectos" });
            }

            // Si las credenciales son correctas, devolver una respuesta exitosa
            return Ok(new { message = "Login exitoso", userId = user.Id, nombre = user.Nombre });
        }

        // DELETE: api/Users/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteUser(int id)
        {
            var user = await _context.Users.FindAsync(id);
            if (user == null)
            {
                return NotFound();
            }

            _context.Users.Remove(user);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool UserExists(int id)
        {
            return _context.Users.Any(e => e.Id == id);
        }
    }

    // DTO para recibir las credenciales de login
    public class LoginDto
    {
        public string Email { get; set; }
        public string Password { get; set; }
    }

    // DTO para los detalles del usuario
    public class UserDetailsDto
    {
        public string Nombre { get; set; }
        public string Apellidos { get; set; }
        public string Region { get; set; }
        public string CorreoElectronico { get; set; }
        public List<DireccionEntregaDto> DireccionesEntrega { get; set; }
    }

    // DTO para la entidad DireccionEntrega
    public class DireccionEntregaDto
    {
        public int Id { get; set; }
        public string Calle { get; set; }
        public string Ciudad { get; set; }
        public string CodigoPostal { get; set; }
        public string Pais { get; set; }
    }
}
