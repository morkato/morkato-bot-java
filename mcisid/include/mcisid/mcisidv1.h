#include <stdbool.h>
#include <stdint.h>
#include "./mcisid.h"

#ifndef MCISID_V1_H
#define MCISID_V1_H

#define MCISIDV1_RESERVED_SEQ_LIMIT  1024
#define MCISIDV1_MAX_SEQUENCE        0x3FFFF
#define MCISIDV1_START_SEQUENCE      (MCISIDV1_RESERVED_SEQ_LIMIT + 1)
#define MCISIDV1_GENERATOR_SEQUENCES 64
#define MCISIDV1_IDENTIFER           0
#define MCISIDV1_IOSIZE              12
#define MCISIDV1_INVALID_SEQVALUE    0
#define MCISIDV1_INVALID_ID          "000000000000"

/*
 * Estado de retorno da função mcisidv1Generate e mcisidv1NextValue:
 * Indica que o ID foi gerado com sucesso dentro do mesmo instante de tempo.
 */
#define MCISIDV1_SUCCESS 0
/*
 * Estado de retorno da função mcisidv1Generate e mcisidv1NextValue:
 * A sequência atingiu o valor máximo permitido para o instante atual. 
 * É necessário aguardar o próximo milisegundo.
 */
#define MCISIDV1_OVERFLOW 1
/*
 * Estado de retorno da função mcisidv1Generate e mcisidv1NextValue:
 * A sequência atingiu o limite anterior, porém o instante de tempo já avançou.
 * É seguro reiniciar a sequência.
 */
#define MCISIDV1_RESET 2
/*
 * Estado de retorno da função mcisidv1Generate e mcisidv1NextValue:
 * O instante de tempo atual é anterior ao último instante registrado.
 * Isso pode ocorrer devido à alteração no relógio do sistema (NIT), do Epoch definido pelo usuário (UDE) ou imprecisão (retrocesso) do relógio.
 */
#define MCISIDV1_INSTANT_CORRUPTED 4
/*
 * Estado de retorno da função mcisidv1Generate e mcisidv1NextValue:
 * O icremento da sequência é inválido. Esse erro nunca irá ocorrer caso a aplicação cumpra as regras.
 * Provavelmente, você esqueceu de resetar a sequência de forma segura.
 */
#define MCISIDV1_STUPID_IMPOSSIBLE_SCENARY 5

#define MCISIDV1_COMPARE_LESS   -1
#define MCISIDV1_COMPARE_EQUAL   0
#define MCISIDV1_COMPARE_GREATER 1

#if defined(__unix)
  #define MCISIDV1_BASE_EPOCH_UNIX_MS 1704067200000ULL // 2024-01-01T00:00:00Z
#elif defined(_WIN32)
  #define MCISIDV1_BASE_EPOCH_WINDOWS_MS /* Não sei */ 1704067200000ULL
#endif

/*
 * ESTRUTURA DO MCISIDV1 (VERSÃO 1)
 *
 * IDENTIFIER (6 bits)  -> Sempre 0 para a versão V1.
 * MODEL      (6 bits)  -> Indica o tipo/origem do objeto no banco. Não representa um nó.
 * SEQUENCE   (18 bits) -> Incrementado para garantir unicidade dentro do mesmo milisegundo.
 *                         Reservado: 0 - 1024 (usado internamente).
 * TIMESTAMP  (42 bits) -> Tempo em milisegundos desde um User Defined Epoch (UDE).
 *                         Duração máxima estimada: ~130 anos a partir do UDE.
 *
 * TAMANHO FIXO: 12 BYTES (72 BITS BRUTO) - Conversão de caracteres por bs64
 * ESTRUTURA BINÁRIA: [IDENTIFIER: 6b] [MODEL: 6b] [SEQUENCE: 18b] [TIMESTAMP: 42b]
 */
typedef struct __attribute__((packed)) _mcisidv1 {
  char identifier;
  char model;
  char sequence[3];
  char timestamp[7];
} _mcisidv1;
typedef char mcisidv1[MCISIDV1_IOSIZE];

_Static_assert(sizeof(_mcisidv1) == MCISIDV1_IOSIZE, "O tamanho de _mcisidv1 deve conter o mesmo espaço reservado no macro MCISIDV1_IOSIZE.");
_Static_assert(sizeof(mcisidv1) == MCISIDV1_IOSIZE, "O tamanho de mcisidv1raw deve conter o mesmo espaço reservado no macro MCISIDV1_IOSIZE.");

typedef int8_t  mcisidv1_vmodel;
typedef int32_t mcisidv1_vsequence;
typedef int64_t mcisidv1tm;
typedef int32_t mcisidv1cmp;

typedef struct mcisidv1seq {
  int32_t    current;
  mcisidv1tm lastime;
} mcisidv1seq;

typedef struct mcisidv1snapshot {
  mcisidv1_vmodel    model;
  mcisidv1_vsequence seqnext;
  mcisidv1tm         instant;
} mcisidv1snapshot;

mcisidv1tm       mcisidv1GetSystemInstant    ();
mcisidstate      mcisidv1NextValue           (mcisidv1seq* sequence, mcisidv1tm instant);
void             mcisidv1ResetSequence       (mcisidv1seq* sequence);
mcisidv1snapshot mcisidv1TakeSnapshot        (const mcisidv1seq* seq, mcisidv1_vmodel model);
mcisidstate      mcisidv1Generate            (mcisidv1seq* sequence, mcisidv1_vmodel model, mcisidv1tm instant, mcisidv1 output);
void             mcisidv1WriteOutput         (const mcisidv1snapshot* snapshot, mcisidv1 output);
bool             mcisidv1IsInvalid           (const mcisidv1 mcisid);
mcisidv1_vmodel  mcisidv1GetOriginModel      (const mcisidv1 input);
int32_t          mcisidv1GetSequence         (const mcisidv1 input);
mcisidv1tm       mcisidv1GetTimeInstantMilis (const mcisidv1 input);
mcisidv1cmp      mcisidv1Compare             (const mcisidv1 a, const mcisidv1 b);

#endif // MCISID_V1_H
