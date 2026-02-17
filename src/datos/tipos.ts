export interface ElementoMenu {
  nombre: string;
  precio: number;
}

export interface ElementoPedido extends ElementoMenu {
  cantidad: number;
}

export type Pantalla = "inicio" | "menu" | "stock" | "pago";
