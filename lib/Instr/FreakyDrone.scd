Instr("FreakyDrone", {

  arg baseFreq, baseModFreq;

  var carrier,
    modulator,
    modulatorModulator,
    out,
    modFreq,
    freq;
  
  modulatorModulator = SinOsc.ar(Rand(0.02, 0.1));
  modFreq = baseModFreq + modulatorModulator.range(0, 0.33 * baseModFreq);
  
  modulator = LFTri.ar(modFreq);
  freq = baseFreq + modulator.range(0, 1.33 * baseFreq);

  carrier = SinOsc.ar(freq);

  out = carrier;
}, [
  \freq,
  \freq
]);

