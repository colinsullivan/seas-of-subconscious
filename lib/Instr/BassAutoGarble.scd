/**
 *  @file       BassAutoGarble.scd
 *
 *              Attempt at FFT shuffling based on input amplitude envelope.
 *              Works OK but not great.
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

Instr("BassAutoGarble", {
  arg buf,
    gate,
    playbackRate,
    startTime,
    attackTime,
    releaseTime,
    convertToStereo;

  var out,
    ampTracked,
    filtModulator,
    env;

  out = Instr.ar("cs.sfx.PlayBuf", (
    buf: buf,
    gate: gate,
    playbackRate: playbackRate,
    startTime: startTime,
    attackTime: attackTime,
    releaseTime: releaseTime
  ));

  ampTracked = Amplitude.kr(
    in: out,
    attackTime: 0.1,
    releaseTime: 0.1
  ).lagud(0.1, 0.1);

  filtModulator = SinOsc.ar(1.0 * ampTracked);

  out = BPeakEQ.ar(out,
    freq: filtModulator.range(50, 8000),
    rq: 0.5,
    db: 12
  );

  out = (0.7 * out) + (0.3 * Instr.ar("cs.fx.FFT.Shuffle", (
    in: out,
    scrambleAmt: filtModulator.range(0.0, 2.0),
    scrambleWidth: filtModulator.range(0.0, 1.0),
    gate: gate
  )));

  env = Linen.kr(gate, doneAction: 2);

  out = env * out;

  /*out = Instr.ar("cs.fx.LPFerModulated", (*/
    /*in: out,*/
    /*cutoffMinFreq: 50,*/
    /*cutoffMaxFreq: 5000,*/
    /*cutoffModFreq: 2.0*/
  /*));*/

  [out, out];

}, [
  \buffer,
  \gate,
  ObjectSpec(1.0),
  ObjectSpec(0.0),
  ObjectSpec(1.0),
  ObjectSpec(1.0)
]);
