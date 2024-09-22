export interface CommentaireDto {
  id?: number;
  contenu: string;
  dateCreation: Date | null;
  utilisateurName?: string;
  annonceId?: number;
}
