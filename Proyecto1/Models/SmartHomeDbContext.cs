using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;

namespace Proyecto1.Models
{
    public class SmartHomeDbContext : DbContext
    {

            public SmartHomeDbContext(DbContextOptions<SmartHomeDbContext> options) :base(options)
            {
            }

            public DbSet<User> Users { get; set; }
    }
}
