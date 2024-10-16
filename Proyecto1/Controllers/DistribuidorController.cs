using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Proyecto1.Models;

namespace Proyecto1.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DistribuidorController : ControllerBase
    {
        private readonly SmartHomeDbContext _context;

        public DistribuidorController(SmartHomeDbContext context)
        {
            _context = context;
        }

        // GET: api/Distribuidor
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Distribuidor>>> GetDistribuidores()
        {
            return await _context.Distribuidores.ToListAsync();
        }

        // GET: api/Distribuidor/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Distribuidor>> GetDistribuidor(int id)
        {
            var distribuidor = await _context.Distribuidores.FindAsync(id);

            if (distribuidor == null)
            {
                return NotFound();
            }

            return distribuidor;
        }

        // POST: api/Distribuidor
        [HttpPost]
        public async Task<ActionResult<Distribuidor>> PostDistribuidor(Distribuidor distribuidor)
        {
            _context.Distribuidores.Add(distribuidor);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetDistribuidor", new { id = distribuidor.Id }, distribuidor);
        }

        // PUT: api/Distribuidor/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutDistribuidor(int id, Distribuidor distribuidor)
        {
            if (id != distribuidor.Id)
            {
                return BadRequest();
            }

            _context.Entry(distribuidor).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DistribuidorExists(id))
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

        // DELETE: api/Distribuidor/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteDistribuidor(int id)
        {
            var distribuidor = await _context.Distribuidores.FindAsync(id);
            if (distribuidor == null)
            {
                return NotFound();
            }

            _context.Distribuidores.Remove(distribuidor);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool DistribuidorExists(int id)
        {
            return _context.Distribuidores.Any(e => e.Id == id);
        }
    }
}
