(
  var m, mBounds;
 
  s.quit;
  
  /*s.options.inDevice = "PreSonus FIREPOD (2112)";*/
  /*s.options.inDevice = "SF + 1818";*/
  /*s.options.inDevice = "AudioBox 1818 VSL ";*/
  /*s.options.inDevice = "Soundflower (64ch)";*/

  s.options.outDevice = "Soundflower (64ch)";
  s.options.sampleRate = 48000;
  s.options.blockSize = 8;
  /*s.options.hardwareBufferSize = 128;*/
  s.boot();
  m = s.meter();

  // move level meter to bottom right of screen
  mBounds = m.window.bounds;
  /*mBounds.left = 1680;
  mBounds.top = 1000;*/
  mBounds.left = 1440;
  mBounds.top = 900;
  
  m.window.setTopLeftBounds(mBounds);

  Instr.dir = "~/Projects/seas-of-subconscious/lib/";
  Instr.loadAll();
  Library.postTree;
  
  s.doWhenBooted({
    ({
      


      Instr("SeasOfSubconciousDrone", {
        var out;
        
        out = Instr.ar("FreakyDroneMultiple", (
          baseFreq: 110,
          baseModFreq: 80,
          numHarms: 7
        ));

        out = Instr.ar("cs.fx.LPFerModulated", (
          in: out,
          cutoffMinFreq: 100,
          cutoffMaxFreq: 125,
          cutoffModFreq: 0.05
        ));

        /*out = Instr.ar("reverbs.reverberator3", (
          audio: out,
          revBalance: 0.6,
          revTime: 8
        ));*/

      });

      Patch("SeasOfSubconciousDrone");
      
    }.play();)
  
  });

)



