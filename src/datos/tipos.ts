export interface ElementoMenu {
  nombre: string;
  precio: number;
  id: number;
  imagen?: string;
  categoriaId?: number;
  categoriaNombre?: string;
}

export interface ElementoPedido extends ElementoMenu {
  cantidad: number;
}

export type Pantalla = "inicio" | "menu" | "stock" | "pago";
