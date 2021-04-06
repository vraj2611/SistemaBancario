package edu.iff.sistemabanco.service;

// TENTATIVA DE CRIAR SERVICO GENERICO PARA FAZER CRUD BASICO QUE OS OUTROS SERVICOES HERDEM
// EU FIZ ISSO NO MEU PROJETO DO SERVIÇO, EM TYPESCRIPT, NOS FRAMEWORKS NEST.JS E NO ANGULAR
// AQUI ESTA COMPLICANDO POIS CADA SERVICE PRECISA DE UM REPOSITORIO QUE ESTENDA DE JPAREPOSITORY
// E PARA DECLARAR UM JPAREPOSITORY, TENHO QUE INFROMAR AS CLASSES
// OU SEJA, NÂO ESTOU CONSEGUINDO USAR GENERICS A APARTIR DE UMA CLASSE QUE JA USA GENERICS COM DUAS CLASSES DISTINTAS 

public class GenericService<T1> {

	/*
	private JpaRepository<?, ?> repo;
	
	public GenericService( JpaRepository<?, ?> repo) {
		this.repo = repo;
		
	}

	public T1 save(T1 entity) {
		try {
			return (T1) this.repo.save(entity); .save((S) entity);	
		} catch(Exception e) {
			throw new RuntimeException("Erro ao salvar Entidade!");
		}
		
	}
	
 * 
 * 
 * 
	@SuppressWarnings("unchecked")
	public List<T1> findAll(){
		return (List<T1>) this.getRepo().findAll();
	}

	protected JpaRepository<?, ?> getRepo() {
		return null;
	}
	
	public S save(S entity) {
		try {
			return (S) this.getRepo().save((S) entity);	
		} catch(Exception e) {
			throw new RuntimeException("Erro ao salvar Entidade!");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<T1> findAll(int page, int size){
		Pageable p = PageRequest.of(page, size);
		return (List<T1>) repo.findAll(p).toList();
	}

	public T1 findById(Long id){
		 Optional<T1> result = repo.getOne((Long) id);
		 if(result.isEmpty()) throw new RuntimeException("Entidade nao encontrada!");
		 return (T1) result.get();
	}
	
	
	
	public T1 update(T1 entity) {
		try {
			return (T1) repo.save((T1) entity);
		} catch(Exception e) {
			throw new RuntimeException("Erro ao atualizar Conta!");
		}
	}
	
	public void delete(Long id) {
		Conta c = findById(id);
		checkDeleteConta(c);
		try {
			repo.delete(c);			
		} catch(Exception e) {
			throw new RuntimeException("Erro ao excluir Conta!");
		}
		
	}
*/
	
}
