﻿using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Proyecto1.Migrations
{
    /// <inheritdoc />
    public partial class _15 : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_DireccionesEntrega_Users_UserId",
                table: "DireccionesEntrega");

            migrationBuilder.AlterColumn<int>(
                name: "UserId",
                table: "DireccionesEntrega",
                type: "integer",
                nullable: true,
                oldClrType: typeof(int),
                oldType: "integer");

            migrationBuilder.AddForeignKey(
                name: "FK_DireccionesEntrega_Users_UserId",
                table: "DireccionesEntrega",
                column: "UserId",
                principalTable: "Users",
                principalColumn: "Id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_DireccionesEntrega_Users_UserId",
                table: "DireccionesEntrega");

            migrationBuilder.AlterColumn<int>(
                name: "UserId",
                table: "DireccionesEntrega",
                type: "integer",
                nullable: false,
                defaultValue: 0,
                oldClrType: typeof(int),
                oldType: "integer",
                oldNullable: true);

            migrationBuilder.AddForeignKey(
                name: "FK_DireccionesEntrega_Users_UserId",
                table: "DireccionesEntrega",
                column: "UserId",
                principalTable: "Users",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
