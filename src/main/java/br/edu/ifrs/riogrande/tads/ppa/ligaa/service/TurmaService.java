package br.edu.ifrs.riogrande.tads.ppa.ligaa.service;

import org.springframework.stereotype.Service;

import br.edu.ifrs.riogrande.tads.ppa.ligaa.entity.Matricula;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.entity.Matricula.Situacao;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.entity.Turma;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.repository.AlunoRepository;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.repository.DisciplinaRepository;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.repository.ProfessorRepository;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.repository.TurmaRepository;
import jakarta.annotation.PostConstruct;

@Service
public class TurmaService {

    int numero;

    private final TurmaRepository turmaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    
    public TurmaService
        (
            TurmaRepository turmaRepository,
            DisciplinaRepository disciplinaRepository,
            AlunoRepository alunoRepository,
            ProfessorRepository professorRepository
        ) {
        this.turmaRepository = turmaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
    }

    public Matricula matricular(String cpf, String codigoTurma) {
        
        // turma existe?
        var turma = turmaRepository.findByCodigo(codigoTurma).orElseThrow(() -> new NotFoundException());

        // turma já terminou o ciclo?
        if (turma.isFechada()) {
            throw new ServiceException("Turma " + codigoTurma + " está fechada");
        }

        // aluno existe?
        var aluno = alunoRepository.findByCpf(cpf)
            .orElseThrow(() -> new NotFoundException());

        // aluno já matriculado?
        if (turma.getMatriculas().stream().anyMatch(m -> m.getAluno().equals(aluno))) {
            throw new ServiceException("Aluno " + cpf + " já está matriculado na turma " + codigoTurma);
        }

        // todas as turmas do aluno
        var turmas = turmaRepository.findByAluno(aluno);

        // aluno já fez essa disciplina?
        if (turmas.stream().flatMap(t -> t.getMatriculas().stream())
            .anyMatch(m -> m.getAluno().equals(aluno) && m.getSituacao().equals(Situacao.APROVADO))) {
            throw new ServiceException("Aluno " + cpf + " já aprovado na disciplina " + turma.getDisciplina().getNome());
        }

        int qtdAlunosTurma = turma.getMatriculas().size();
        
        int qtdVagas = turma.getVagas();

        // há vagas?
        if (qtdAlunosTurma >= qtdVagas) { // não
            // turmas anteriores da disciplina
            var turmasAnterioresDaDisciplina = turmas.stream()
                .filter(t -> t.getDisciplina().equals(turma.getDisciplina()))
                .toList();

            boolean reprovadoAnteriormente = turmasAnterioresDaDisciplina.stream().flatMap(t -> t.getMatriculas().stream())
                        .anyMatch(m -> m.getAluno().equals(aluno) && m.getSituacao().equals(Situacao.REPROVADO));
            
            // se nunca foi reprovado, não pode matricular-se
            if (!reprovadoAnteriormente) {
                throw new ServiceException("Não há vagas na turma " + codigoTurma);
            }
        }

        var matricula = new Matricula();
        matricula.setNumero(++numero);
        matricula.setSituacao(Situacao.REGULAR);
        matricula.setAluno(aluno);
        turma.getMatriculas().add(matricula);
        return matricula;
    }


    @PostConstruct
    void popular() {
        var can = alunoRepository.findByCpf("11122233344").orElseThrow();
        var ppa = disciplinaRepository.findByCodigo("ppa").orElseThrow();
        var marcio = professorRepository.findBySiape("1810497").orElseThrow();
        
        var ppa20242 = new Turma();
        ppa20242.setCodigo("ppa-2024-2");
        ppa20242.setDisciplina(ppa);
        ppa20242.setProfessor(marcio);
        ppa20242.setSemestre("2024-2");
        ppa20242.setVagas(1);

        var mat = new Matricula();
        mat.setAluno(can);
        mat.setNumero(++numero);
        mat.setSituacao(Situacao.REPROVADO);

        ppa20242.getMatriculas().add(mat);

        turmaRepository.save(ppa20242);
    }

    
}
