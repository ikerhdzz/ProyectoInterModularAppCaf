export interface ElementoMenu {
  nombre: string;
  precio: number;
}

export interface ElementoPedido extends ElementoMenu {
  cantidad: number;
}